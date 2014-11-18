import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import fisica.*; 
import TUIO.*; 
import processing.opengl.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class bacterias extends PApplet {




 

TuioProcessing tuioClient;
FWorld mundo;

int timer;
int cual;

ArrayList<Filamento> filamentos;
ArrayList<Coco> cocos;
ArrayList<Comida> comidas;
ArrayList<Comida> desechos;
ArrayList<Vampiro> vampiros;

int colorFilamentos = color(191,72,83);
int colorCocos = color(233,161,76);

PVector crear;

PImage over;

public void setup() {
	size(900, 650);
	smooth();
	frameRate(30);
	///------TUIO-----------
	tuioClient  = new TuioProcessing(this);
	//---------------------

	over = loadImage("data/overlay.png");

	Fisica.init(this);

	mundo = new FWorld();
	mundo.setEdges(color(255,255,255,0));
	mundo.setGravity(0,0);

	filamentos = new ArrayList<Filamento>(); 
	cocos = new ArrayList<Coco>();
	comidas = new ArrayList<Comida>(); 
	desechos = new ArrayList<Comida>();
	vampiros = new ArrayList<Vampiro>();


	crear = new PVector(width/2,height/2);


}

public void draw() {

	background(color(235,235,244));	

	mundo.step();
	mundo.draw();


	if(desechos.size()>1){/// si hay desechos buscamos y accionamos los filamentos
		/*-FILAMENTOS-*/
		for (int i = filamentos.size()-1; i >= 0; i--) {
			Filamento esteFilamento = filamentos.get(i);
			esteFilamento.mover(filamentos);
			esteFilamento.sigueVivo();
			esteFilamento.buscarComida(desechos);
		}
	}

	if(comidas.size()>1){  /// si hay comida buscamos y accionamos los cocos
		
		/*-COCOS-*/
		for (int i = cocos.size()-1; i >= 0; i--) {
			Coco esteCoco = cocos.get(i);
			esteCoco.mover(cocos);
			esteCoco.buscarComida(comidas);
			esteCoco.desecho(desechos);
		}

	}

	/*if(cocos.size()>1){ /// si hay cocos buscamos y accionamos los vampiros

		/*-VAMPIROS-*/
		for (int i = vampiros.size()-1; i >= 0; i--) {
			Vampiro esteVampiro = vampiros.get(i);
			esteVampiro.mover(vampiros);
			esteVampiro.comer(cocos);
		}
	

	
	
	image(over, 0, 0, width, height);
}

public void keyPressed(){

	switch (key) {
		case 'f' :
			filamentos.add(new Filamento(mundo,5,filamentos.size()));
		break;	
		case 'F' :
			int cf = PApplet.parseInt(random(filamentos.size()));
			Filamento esteFilamento= filamentos.get(cf);
			esteFilamento.matar();
		break;	
		case 'c' :
			cocos.add(new Coco(mundo,20, cocos.size()));			
		break;	
		case 'C' :
			int cc = PApplet.parseInt(random(cocos.size()));
			Coco esteCoco= cocos.get(cc);
			esteCoco.matar();
		break;	
		case 'm' :
			comidas.add(new Comida(mundo));
		break;	
		case 'v' :
			vampiros.add(new Vampiro(mundo,crear));
		break;	
		case 'r' :
		
			for (int i = filamentos.size()-1; i >= 0; i--) {
				Filamento eFilamento =filamentos.get(i);
				eFilamento.matar();
			}
			for (int i = cocos.size()-1; i >= 0; i--) {
				Coco eCoco =cocos.get(i);
				eCoco.matar();
			}
		break;	
	}
}

public void removeTuioCursor(TuioCursor tcur) {
	//println(tcur);
  	float _tx = tcur.getX()*width;
  	float _ty = tcur.getY()*height;
  	crear.x = _tx;
  	crear.y = _ty;
  	if(dist(_tx,_ty,width/2,height/2)<100){
  		comidas.add(new Comida(mundo,crear));
  	}

  	cual = cual > 2 ? 0: cual+1;
  	if(dist(_tx,_ty,width/2,height/2)>100){
  		switch (cual) {
			case 0:
				filamentos.add(new Filamento(mundo,5,crear));
			break;	
			case 1:			
				vampiros.add(new Vampiro(mundo,crear));
			break;	
			case 2:
				cocos.add(new Coco(mundo,20,crear));			
			break;	
		}
  	}
  	
}


public void updateTuioCursor(TuioCursor tcur){
	float _tx = tcur.getX()*width;
  	float _ty = tcur.getY()*height;
  	
  		//atraer cocos
  		for (int i = cocos.size()-1; i >= 0; i--) {
			Coco esteCoco = cocos.get(i);
			PVector cocoPos = esteCoco.getPos();
			if(dist(_tx,_ty,cocoPos.x,cocoPos.y)< 30){
				esteCoco.seguir(_tx,_ty);
			}
		}


}


public void crearOrganismo(int _c,PVector _crear){
	switch (_c) {
		case 0:
			filamentos.add(new Filamento(mundo,5,_crear));
		break;	
		case 1:			
			vampiros.add(new Vampiro(mundo,_crear));
		break;	
		case 2:
			cocos.add(new Coco(mundo,20,_crear));			
		break;	
	}
}
class Coco {
	boolean coco = true;
	boolean buscaComida = true;
	float x = 0.0f;
	float y = 0.0f;
	float dir = 0.0f;
	float aceleracionX = 0.0f;
	float aceleracionY = 0.0f;
	int c = color(233,161,76);
		
	float d ;
	float frequency = 10;
	float damping = 0.1f;

	float offset = 10;
	float vAng = radians(PI);

  	float nDir;
  	String nombre;
	FWorld m;
	FBody cuerpo;
  	Boolean vive = true;

  	int timer;
  	PImage pelos;
  	int energia = 10;

	Coco (FWorld _m, float _d, int _id) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		nombre = "coco_"+_id;
		pelos = loadImage("data/bola.png");
		crearCoco();
	}

	Coco (FWorld _m, float _d) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		nombre = "coco";
		pelos = loadImage("data/bola.png");

		crearCoco();
	}

	Coco (FWorld _m, float _d, PVector _pos) {
		m = _m;
		x = _pos.x;
		y = _pos.y;
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		nombre = "coco";
		pelos = loadImage("data/bola.png");

		crearCoco();
	}




	/*-COCO-*/
	public void crearCoco(){
	    cuerpo = new FCircle(d);
	    cuerpo.setPosition(x, y);
	    cuerpo.setNoStroke();
	    cuerpo.setNoFill();
	    cuerpo.setDensity(d/100);
	    cuerpo.setName(nombre);
	    //cuerpo.attachImage(pelos);
	    m.add(cuerpo);

	}

	public void dibujar(){
		pushStyle();
		pushMatrix();
			float escala = map(d,6,80,0.4f,1);
			translate(cuerpo.getX(), cuerpo.getY());
			//ellipse(0,0,d,d);
			scale(escala);
			imageMode(CENTER);
			image(pelos,0,0);
		popMatrix();
		popStyle();
	}

	public void mover(ArrayList<Coco> _c){
		timer = millis();

		if(coco){
			dibujar();
		}

		if(timer%101 == 100){
			energia-=5;
		}

		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05f;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 


	 	/*aceleracionX =  x > width - offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = 	y > height - offset ? aceleracionY*-1 : aceleracionY ;
	 	aceleracionX =  x < offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = 	y < offset ? aceleracionY*-1 : aceleracionY ;*/
	 	if(dist(width/2,height/2,x,y) > height/2){
	    	aceleracionX = aceleracionX*-1;
	    	aceleracionY = aceleracionY*-1;	
	    }

		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);

		cuerpo.addTorque(dx*5);
		float xa = cuerpo.getX();
		float ya = cuerpo.getY();
        x+=dx;
        y+=dy;
        if(buscaComida || !coco){
        	xa+=(x-xa)*0.2f;
        	ya+=(y-ya)*0.2f;
       		cuerpo.setPosition(xa,ya);
        }else{
       		cuerpo.addForce(dx*(energia*0.2f),dy*(energia*0.2f));  //buscar la magnitud valor
       		energia -- ;
       		if(energia<0){
				_c.remove(this);
			} 
        }

	}

	public void setPos(float _x, float _y){
		x= _x;
		y=_y;

		cuerpo.setPosition(x,y);
	}

	public void seguir(float _xpos,float _ypos){
		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05f;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 

		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);

		cuerpo.addForce(dx*(energia*0.2f),dy*(energia*0.2f));
	}

	public PVector getPos(){
		PVector salida = new PVector(cuerpo.getX(),cuerpo.getY());
		return salida;
	}

	public void changeColor(int _c){
		c = _c;
	}

	public void changeRadio(float _d){
		d = _d;
	}

	public float diam(){
		return d;
	}


	public void matar(){
		m.remove(cuerpo);
	}

	public void buscarComida(ArrayList<Comida> _comida){
		if(energia<100){
			buscaComida = true;	
			float _tx = 0.0f;
			float _ty = 0.0f;

			Comida estaComida = _comida.get(_comida.size()-1);
			Comida otraComida = _comida.get(0);

			float _etx = estaComida.pos().x;
			float _ety = estaComida.pos().y;

			float _otx = otraComida.pos().x;
			float _oty = otraComida.pos().y;

			if(dist(cuerpo.getX(),cuerpo.getY(),_etx,_ety)< dist(cuerpo.getX(),cuerpo.getY(),_otx,_oty)){
				x += (_etx-x)*0.05f;
				y += (_ety-y)*0.05f;
				_tx = _etx;
				_ty = _ety;		
				if(dist(cuerpo.getX(),cuerpo.getY(),_tx,_ty)< d +10){					
					energia += estaComida.darEnergia(_comida);
				}		
			}else{
				x += (_otx-x)*0.05f;
				y += (_oty-y)*0.05f;
				_tx = _otx;
				_ty = _oty;
				if(dist(cuerpo.getX(),cuerpo.getY(),_tx,_ty)< d +10){					
					energia += otraComida.darEnergia(_comida);
				}
			}	

			
		}else{
			buscaComida = false;
		}			

		
	}

	public void addImage(PImage _i){
		energia = 100;
		coco = false;
		cuerpo.setNoStroke();
		cuerpo.setNoFill();
		cuerpo.attachImage(_i);
	}


	public void desecho(ArrayList<Comida> _desecho){
		if(!buscaComida && _desecho.size()<10){
			energia-=80;
			_desecho.add(new Comida(mundo,cuerpo.getX()-d,cuerpo.getY()-d));
		}
	}

	public FBody cuerpo(){
		return cuerpo;
	}
}
class Comida {
	FWorld mundo;
	FBody centro;
	FBody comida;
	String nombre;
	int d = 6;
	int c = color(0,208,140);
	int cStroke = color(0,125,0);

	int c2 = color(73,0,34);

	int borde = 2;
	int energia = 5;
	int energiaValor = 100;
	float x = 0.0f;
	float y = 0.0f;
	PVector position;

	ArrayList<FCircle> comidas;

	Comida (FWorld _m) {
		mundo = _m;
		comidas = new ArrayList<FCircle>();
		nombre = "comida";
		x = random(50, width-50);		
		y = random(50, height-50);
		crearComida(true);
	}
	Comida (FWorld _m, PVector _pos) {
		mundo = _m;
		comidas = new ArrayList<FCircle>();
		nombre = "comida";
		x = _pos.x;		
		y =_pos.y;
		crearComida(true);
	}


	Comida (FWorld _m , float _x,float _y) {
		mundo = _m;
		comidas = new ArrayList<FCircle>();
		nombre = "desecho";
		x =_x;		
		y =_y;
		c= c2;
		crearComida(false);
	}

	public void crearComida(boolean _borde){
		centro = new FCircle(d);
	 	centro.setPosition(x,y);
	 	centro.setFill(red(c),green(c),blue(c));
	 	if(_borde){
	 		centro.setStroke(borde);
			centro.setStrokeColor(cStroke);
	 	}else{
	 		centro.setNoStroke();
	 	}
	 	mundo.add(centro);

		for (int i = 0; i < energia; ++i) {
			d =PApplet.parseInt(random(4,8));
			comidas.add(new FCircle(d));		    
		}

		for (int i = comidas.size()-1; i >= 0; i--){
			FCircle tComida = comidas.get(i);
			tComida.setFill(red(c),green(c),blue(c));;
			tComida.setName(nombre);
			tComida.setPosition(x,y);
			if(_borde){		 		
				tComida.setStroke(borde);
				tComida.setStrokeColor(cStroke);
		 	}else{
		 		tComida.setNoStroke();
		 	}
			mundo.add(tComida);

			FDistanceJoint junta = new FDistanceJoint(centro, tComida);
		    junta.setLength(2);
		    junta.setNoStroke();
		    junta.setStroke(0);
		    junta.setFill(0);
		    junta.setDrawable(false);
		    junta.setFrequency(0.8f);
		    mundo.add(junta);
		}
				
		centro.addForce(40,40);
		
	}

	public void actPos(){
		x = centro.getX();
		y = centro.getY();
	}

	public PVector pos(){
		actPos();
		position = new PVector(x,y);
		return position;
	}

	public int darEnergia(ArrayList _donde){
		int _e = energiaValor;

		if(energiaValor < 1){
			for (int i = comidas.size()-1; i >= 0; i--){
				mundo.remove(comidas.get(i));
			}
			mundo.remove(centro);
			_donde.remove(this);
		}else{
			energiaValor--;
			FCircle estaComida = comidas.get(PApplet.parseInt(random(comidas.size())));
			mundo.remove(estaComida);
		}
		
		return _e;
	}

	public int tam(){
		return energia * d;
	}
}
class Decobg extends Coco {
	PImage mancha;
	int i = PApplet.parseInt(random(1,3));
	Decobg (FWorld _m) {
		super(_m,80);
		selectImage();
		super.addImage(mancha);
	}

	public void selectImage(){
		switch (i) {
			case 1 :
				mancha = loadImage("data/mancha-1.png");				
			break;	
			case 2 :
				mancha = loadImage("data/mancha-2.png");				
			break;	
			case 3 :
				mancha = loadImage("data/mancha-3.png");				
			break;	
		}

	}

}
class Filamento {
	float x = 0.0f;
	float y = 0.0f;
	float dir = 0.0f;
	float aceleracionX = 0.0f;
	float aceleracionY = 0.0f;
	int c = color(191,72,83);;
	FWorld m;
	FBody[] partes = new FBody[10];	
	float d ;
	float frequency = 30;
	float damping = 1;

	float offset = 10;
	float vAng = radians(PI);
	Boolean vive = true;
  	float nDir;
  	String name;
  	int energia= 0;
  	boolean buscaComida = true;

	Filamento (FWorld _m, float _d,int _id) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		name = "filamento_"+_id;	
		crearFilamento();
	}

	Filamento (FWorld _m, float _d) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		name = "vampiro";	
		crearFilamento();
	}

	Filamento (FWorld _m, float _d, PVector  _pos) {
		m = _m;
		x = _pos.x;
		y = _pos.y;
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		name = "vampiro";	
		crearFilamento();
	}


	/*-LOMBRIS-*/
	public void crearFilamento(){ //NOMBRES para todos iguales!!!
		for (int i=0; i<partes.length; i++) {
		    partes[i] = new FCircle(d);
		    partes[i].setPosition(x, y);
		    partes[i].setNoStroke();
		    partes[i].setStrokeWeight(0);
		    partes[i].setFill(red(c),green(c),blue(c));
		    partes[i].setGroupIndex(1);
		    partes[i].setDensity(aceleracionX);
		    partes[i].setName(name);
		    m.add(partes[i]);
	  	}

		for (int i=1; i<partes.length; i++) {
		    FDistanceJoint junta = new FDistanceJoint(partes[i-1], partes[i]);
		    junta.setAnchor1(-d/2, 0);
		    junta.setAnchor2(d/2, 0);
		    junta.setStrokeWeight(d);
		    junta.setStrokeColor(c);
		    junta.setFrequency(frequency);
		    junta.setDamping(damping);
		    junta.setFill(red(c),green(c),blue(c));
		    junta.setLength(0);
		    m.add(junta);
		    //trabajar con attachImage y jugar con el tama\u00f1o de cada objeto
	  	}
	}


	public void mover(ArrayList<Filamento> _f){
		energia -- ;
		if(energia<0){
			_f.remove(this);
		}
		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05f;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 

	    if(dist(width/2,height/2,x,y) > height/2){
	    	aceleracionX = aceleracionX*-1;
	    	aceleracionY = aceleracionY*-1;	
	    }

		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);



        x+=dx;
        y+=dy;
        //ellipse(x, y, d, d);

       /*	PVector posCabeza = new PVector(partes[0].getX(),partes[0].getY());
       	PVector posCola = new PVector(partes[partes.length-1].getX(),partes[partes.length-1].getY());
       	if(dist(posCabeza.x, posCabeza.y,posCola.x,posCabeza.y) > partes.length*d){
   			for (int i=0; i<partes.length; i++) {
   				partes[i].setFill(red(c),green(c),blue(c),50);
   				vive = false;
   			}
       	}else{*/
       		if(buscaComida){
       			partes[0].setPosition(x,y);
       		}else{
       			partes[0].addImpulse(dx*0.2f,dy*0.2f);
	        }
       	/*}*/

	}

	public void setPos(float _x, float _y){
		x= _x;
		y=_y;
		partes[0].setPosition(x,y);
	}

	public void changeColor(int _c){
		c = _c;
	}

	public void changeRadio(float _d){
		d = _d;
	}

	public float diam(){
		return d;
	}

	public void sigueVivo(){
		if(!vive){
			for (int i=0; i<partes.length; i++) {
				m.remove(partes[i]);
			}
		}
	}

	public void matar(){
		for (int i=0; i<partes.length; i++) {
			ArrayList<FDistanceJoint> juntas;
			juntas = partes[i].getJoints();
			for (int j = 0; j < juntas.size(); ++j) {
				m.remove(juntas.get(j));
			}			
		}
	}

	public void buscarComida(ArrayList<Comida> _comida){
		if(energia<100){
			buscaComida = true;	
			float _tx = 0.0f;
			float _ty = 0.0f;

			Comida estaComida = _comida.get(_comida.size()-1);
			Comida otraComida = _comida.get(0);

			float _etx = estaComida.pos().x;
			float _ety = estaComida.pos().y;

			float _otx = otraComida.pos().x;
			float _oty = otraComida.pos().y;

			if(dist(partes[0].getX(),partes[0].getY(),_etx,_ety)< dist(partes[0].getX(),partes[0].getY(),_otx,_oty)){
				x += (_etx-x)*0.05f;
				y += (_ety-y)*0.05f;
				_tx = _etx;
				_ty = _ety;		
				if(dist(partes[0].getX(),partes[0].getY(),_tx,_ty)< d +10){					
					energia += estaComida.darEnergia(_comida);
				}		
			}else{
				x += (_otx-x)*0.05f;
				y += (_oty-y)*0.05f;
				_tx = _otx;
				_ty = _oty;
				if(dist(partes[0].getX(),partes[0].getY(),_tx,_ty)< d +10){					
					energia += otraComida.darEnergia(_comida);
				}
			}	

			
		}else{
			buscaComida = false;
		}	
	}
}
class Vampiro{
	float x, y;
	float dir = 0.0f;
	float nDir;
	int c = color(0,72,110);
	PImage img;
	FBody cuerpo;
	float vAng = radians(PI);
	float offset = 10;
	int colas = 4;
	FBody[][] partes = new FBody[colas][5];
	FWorld mundo;

	int ancho = 100;
	int alto = 30;
	int d = 2;
	float aceleracionX;
	float aceleracionY;
	boolean buscaComida = false;

	int energia = 100;

	Vampiro (FWorld _m, PVector pos) {
		mundo = _m;
		x = pos.x;
		y = pos.y;

		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		img = loadImage("data/vampiro.png");
		crearVampiro();
	}

	public void dibujar(){
		pushStyle();
		pushMatrix();
			float escala = map(ancho,50,100,0.4f,1);
			translate(cuerpo.getX(), cuerpo.getY());
			//ellipse(0,0,d,d);
			scale(escala);
			imageMode(CENTER);
			image(img,0,0);
		popMatrix();
		popStyle();
	}

	public void crearVampiro(){
		cuerpo = new FBox(ancho, alto);
		cuerpo.setPosition(x,y);
		cuerpo.setNoStroke();
		cuerpo.setNoFill();
		cuerpo.attachImage(img);
		mundo.add(cuerpo);

		for (int j = 0; j < colas; ++j) {

			for (int i=0; i<partes.length; i++) {
			    partes[j][i] = new FCircle(d);
			    partes[j][i].setPosition(x, y);
			    partes[j][i].setNoStroke();
			    partes[j][i].setStrokeWeight(0);
			    partes[j][i].setFill(red(c),green(c),blue(c));
			    partes[j][i].setGroupIndex(1);
			    mundo.add(partes[j][i]);
		  	}

			for (int i=1; i<partes.length; i++) {

			    FDistanceJoint junta = new FDistanceJoint(partes[j][i-1], partes[j][i]);
			    junta.setAnchor1(-d/2, 0);
			    junta.setAnchor2(d/2, 0);
			    junta.setStrokeWeight(d);
			    junta.setStrokeColor(c);
			    junta.setFrequency(10);
			    junta.setDamping(1);
			    junta.setFill(red(c),green(c),blue(c));
			    junta.setLength(0);
			    mundo.add(junta);
			    //trabajar con attachImage y jugar con el tama\u00f1o de cada objeto
		  	}

			FDistanceJoint juntar = new FDistanceJoint(cuerpo, partes[j][0]);
			juntar.setAnchor1(ancho-40, 0);
		    juntar.setAnchor2(d/2, 0);
			juntar.setStrokeWeight(d);
		    juntar.setStrokeColor(c);
		    juntar.setFrequency(0);
		    juntar.setDamping(1);
			mundo.add(juntar);
		}
		

	}

	public void mover(ArrayList<Vampiro> _v){
		energia -- ;
		if(energia<0){
			_v.remove(this);
		}
		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05f;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 


	 	/*aceleracionX =  x > width - offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY =  y > height - offset ? aceleracionY*-1 : aceleracionY ;
	 	aceleracionX =  x < offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY =  y < offset ? aceleracionY*-1 : aceleracionY ;*/
	 	if(dist(width/2,height/2,x,y) > height/2){
	    	aceleracionX = aceleracionX*-1;
	    	aceleracionY = aceleracionY*-1;	
	    }
		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);

		cuerpo.addTorque(dx*50);


        x+=dx;
        y+=dy;
       		if(buscaComida){
       			cuerpo.setPosition(x,y);
       		}else{
       			cuerpo.addImpulse(dx*10,dy*10);
	        }

	}

	public void comer(ArrayList<Coco> cocos){
		
		for (int i = cocos.size()-1; i >= 0; i--) {
			Coco thisCoco = cocos.get(i);
			PVector pos = thisCoco.getPos();
			if(dist(x,y,pos.x,pos.y)<d){
				mundo.remove(thisCoco.cuerpo());
			}
		}
	}
}
//---------------------------------------------------------------------------------------------------------------------------

public float menorDistAngulos( float origen, float destino ) {
  float distancia = destino - origen;
  return anguloRangoPI( distancia );
}
//---------------------------------------------------------------------------------------------------------------------------

public float anguloRangoPI( float angulo ) {
  float este = angulo;
  for ( int i=0; i<100; i++ ) {
    if ( este > PI ) {
      este -= TWO_PI;
    } else if ( este <= -PI ) {
      este += TWO_PI;
    }
    if ( este >= -PI && este <= PI ) {
      break;
    }
  }
  return este;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "bacterias" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
