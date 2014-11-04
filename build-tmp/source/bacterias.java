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
	mundo.setGravity(0,2);

	filamentos = new ArrayList<Filamento>(); 
	cocos = new ArrayList<Coco>();
	comidas = new ArrayList<Comida>(); 

}

public void draw() {
	background(255);

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
			filamentos.add(new Filamento(mundo,colorFilamentos,5,filamentos.size()));
		break;	
		case 'F' :
			int cf = PApplet.parseInt(random(filamentos.size()));
			Filamento esteFilamento= filamentos.get(cf);
			esteFilamento.matar();
		break;	
		case 'c' :
			cocos.add(new Coco(mundo,colorCocos,50, cocos.size()));			
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
	int c;
		
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

	Coco (FWorld _m, int _c, float _d, int _id) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		c = _c;		
		nombre = "coco_"+_id;
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


       	cuerpo.addForce(dx*100,dy*100);  //buscar la magnitud valor bl ablbla abl

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

}
class Comida {
	FWorld mundo;
	FBody comida;
	String nombre;
	int d = 6;
	int c = color(0,208,140);
	int cStroke = color(0,125,0);
	int energia = PApplet.parseInt(random(5,25));

	Comida (FWorld _m) {
		mundo = _m;
		nombre = "comida";
		crearComida();
	}

	public void crearComida(){
		FBlob liquido = new FBlob();

		 liquido.setPosition(width/2,height/2);
		 liquido.setAsCircle(540,40);
		 liquido.setFill(red(c),green(c),blue(c));
		 mundo.add(liquido);

		for (int i = 0; i < energia; ++i) {
			comida = new FCircle(d);
			comida.setFill(red(c),green(c),blue(c));;
			comida.setName(nombre);
			comida.setPosition(width/2,height/2);
			comida.setStroke(1);
			comida.setStrokeColor(cStroke);
			mundo.add(comida);	
		}
		
	}

}
class Filamento {
	float x = 0.0f;
	float y = 0.0f;
	float dir = 0.0f;
	float aceleracionX = 0.0f;
	float aceleracionY = 0.0f;
	int c;
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

	Filamento (FWorld _m, int _c, float _d,int _id) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		c = _c;	
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
