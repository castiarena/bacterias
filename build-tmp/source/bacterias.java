import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import fisica.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class bacterias extends PApplet {



FWorld mundo;
FWorld bg;
ArrayList<Decobg> decos;


ArrayList<Filamento> filamentos;
ArrayList<Coco> cocos;
ArrayList<Comida> comidas;
int colorFilamentos = color(191,72,83);
int colorCocos = color(233,161,76);

public void setup() {
	size(800, 600);
	smooth();

	Fisica.init(this);

	mundo = new FWorld();
	mundo.setEdges(color(255,255,255,0));
	mundo.setGravity(0,0);

	filamentos = new ArrayList<Filamento>(); 
	cocos = new ArrayList<Coco>();
	comidas = new ArrayList<Comida>(); 

	bg = new FWorld();
	bg.setGravity(0,0);
	bg.setEdges(color(255,255,255,0));


	decos = new ArrayList<Decobg>();
	for (int i = 0; i < 8; i++) {
		decos.add(new Decobg(bg));
	}
}

public void draw() {
	background(color(235,235,244));
	/*-bacground-*/
	for (int i = decos.size()-1; i >= 0; i--) {
		Decobg esteDeco = decos.get(i);
		esteDeco.mover();
	}
	bg.step();
	bg.draw();

	/*-FILAMENTOS-*/
	for (int i = filamentos.size()-1; i >= 0; i--) {
		Filamento esteFilamento = filamentos.get(i);
		esteFilamento.mover();
		esteFilamento.sigueVivo();
	}

	/*-COCOS-*/
	for (int i = cocos.size()-1; i >= 0; i--) {
		Coco esteCoco = cocos.get(i);
		esteCoco.mover();
	}

	mundo.step();
	mundo.draw();
	

	
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
			cocos.add(new Coco(mundo,80, cocos.size()));			
		break;	
		case 'C' :
			int cc = PApplet.parseInt(random(cocos.size()));
			Coco esteCoco= cocos.get(cc);
			esteCoco.matar();
		break;	
		case 'm' :
			comidas.add(new Comida(mundo));
		break;	
	}
}

class Coco {
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

  	PImage pelos;
  	int energia = 0;

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



	/*-COCO-*/
	public void crearCoco(){
	    cuerpo = new FCircle(d);
	    cuerpo.setPosition(x, y);
	    cuerpo.setNoStroke();
	    cuerpo.setFill(red(c),green(c),blue(c));
	    cuerpo.setGroupIndex(1);
	    cuerpo.setDensity(d/100);
	    cuerpo.setName(nombre);
	    cuerpo.attachImage(pelos);
	    m.add(cuerpo);

	}


	public void mover(){

		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05f;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 


	 	aceleracionX =  x > width - offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = 	y > height - offset ? aceleracionY*-1 : aceleracionY ;
	 	aceleracionX =  x < offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = 	y < offset ? aceleracionY*-1 : aceleracionY ;

		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);

		cuerpo.addTorque(dx*5);

       	cuerpo.addForce(dx*300,dy*300);  //buscar la magnitud valor 
        x+=dx;
        y+=dy;
       	//cuerpo.setPosition(x,y);

	}

	public void setPos(float _x, float _y){
		x= _x;
		y=_y;

		cuerpo.setPosition(x,y);
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
		for (int i = _comida.size()-1; i >= 0; i--){
			Comida estaComida = _comida.get(i);

			float _tx = estaComida.pos().x;
			float _ty = estaComida.pos().y;

			if(dist(x,y,_tx,_ty)>d){
				
				energia = estaComida.darEnergia();
			}	
		}

		if(energia>25){

		}
	}

	public void addImage(PImage _i){
		cuerpo.setNoStroke();
		cuerpo.setNoFill();
		cuerpo.attachImage(_i);
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
	int borde = 2;
	int energia = PApplet.parseInt(random(55,100));
	float x = 0.0f;
	float y = 0.0f;
	PVector position;

	Comida (FWorld _m) {
		mundo = _m;
		nombre = "comida";
		x = random(50, width-50);		
		y = random(50, height-50);
		crearComida();
	}

	public void crearComida(){
		centro = new FCircle(d);
	 	centro.setPosition(x,y);
	 	centro.setFill(red(c),green(c),blue(c));
	 	centro.setStroke(borde);
		centro.setStrokeColor(cStroke);
	 	mundo.add(centro);

		for (int i = 0; i < energia; ++i) {
			d =PApplet.parseInt(random(4,8));
			comida = new FCircle(d);
			comida.setFill(red(c),green(c),blue(c));;
			comida.setName(nombre);
			comida.setPosition(x,y);
			comida.setStroke(borde);
			comida.setStrokeColor(cStroke);
			mundo.add(comida);

			FDistanceJoint junta = new FDistanceJoint(centro, comida);
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

	public int darEnergia(){
		int _e = energia;

		return _e;
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
	float frequency = 10;
	float damping = 0.1f;

	float offset = 10;
	float vAng = radians(PI);
	Boolean vive = true;
  	float nDir;
  	String name;

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



	/*-LOMBRIS-*/
	public void crearFilamento(){ //NOMBRES para todos iguales!!!
		for (int i=0; i<partes.length; i++) {
		    partes[i] = new FCircle(d);
		    partes[i].setPosition(x, y);
		    partes[i].setNoStroke();
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
		    junta.setNoStroke();
		    junta.setFrequency(frequency);
		    junta.setDamping(damping);
		    junta.setFill(red(c),green(c),blue(c));
		    junta.setLength(0);
		    m.add(junta);
		    //trabajar con attachImage y jugar con el tama\u00f1o de cada objeto
	  	}
	}


	public void mover(){

		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05f;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 


	 	aceleracionX =  x> width - offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = y>height - offset ? aceleracionY*-1 : aceleracionY ;
	 	aceleracionX =  x< offset  ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = y < offset ? aceleracionY*-1 : aceleracionY ;
		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);



        x+=dx;
        y+=dy;
        //ellipse(x, y, d, d);
       	//partes[0].setPosition(x,y);

       	PVector posCabeza = new PVector(partes[0].getX(),partes[0].getY());
       	PVector posCola = new PVector(partes[partes.length-1].getX(),partes[partes.length-1].getY());
       	if(dist(posCabeza.x, posCabeza.y,posCola.x,posCabeza.y) > partes.length*d){
   			for (int i=0; i<partes.length; i++) {
   				partes[i].setFill(red(c),green(c),blue(c),50);
   				vive = false;
   			}
       	}else{
       		partes[0].addImpulse(dx*0.2f,dy*0.2f);
       	}

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
