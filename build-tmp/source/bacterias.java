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

Filamento[] filamento = new Filamento[50];
int limiteInicio = 5;
Coco[] cocos = new Coco[50];

public void setup() {
	size(800, 600);
	smooth();

	Fisica.init(this);

	mundo = new FWorld();
	mundo.setEdges();
	mundo.setGravity(0,0);


	for (int i = 0; i < filamento.length; ++i) {
		if(i <= limiteInicio){
			filamento[i] = new Filamento(mundo,color(196,69,90), 20);
		}
	}

	for (int i = 0; i < cocos.length; ++i) {
		if(i <= limiteInicio){
			cocos[i] = new Coco(mundo,color(234,162,77), 50);
		}
	}
}

public void draw() {
	background(255);

	for (int i = 0; i < filamento.length; ++i) {
		if(i <= limiteInicio){
			filamento[i].mover();
		}
	}
	

	for (int i = 0; i < cocos.length; ++i) {
		if(i <= limiteInicio){
			cocos[i].mover();
		}
	}

	mundo.step();
	mundo.draw();
}

public void mousePressed(){
	limiteInicio++;
	filamento[limiteInicio] = new Filamento(mundo,color(196,69,90), 5);
	cocos[limiteInicio] = new Coco(mundo,color(234,162,77), 50);
	filamento[limiteInicio].setPos(mouseX +100,mouseY +100);
	cocos[limiteInicio].setPos(mouseX,mouseY);
}
class Coco {
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

  	float nDir;

	Coco (FWorld _m, int _c, float _d) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		c = _c;		
		crearCoco();
	}



	/*-COCO-*/
	public void crearCoco(){
	    partes[0] = new FCircle(d);
	    partes[0].setPosition(x, y);
	    partes[0].setNoStroke();
	    partes[0].setFill(red(c),green(c),blue(c));
	    partes[0].setGroupIndex(1);
	    partes[0].setDensity(0.8f);
	    m.add(partes[0]);

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


       	partes[0].addForce(dx*100,dy*100);  //buscar la magnitud valor bl ablbla abl

        x+=dx;
        y+=dy;
        ellipse(x, y, d, d);
       	//partes[0].setPosition(x,y);

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

  	float nDir;

	Filamento (FWorld _m, int _c, float _d) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9f;
		aceleracionY = 0.9f;
		c = _c;		
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


       	partes[0].addImpulse(dx*0.2f,dy*0.2f);

        x+=dx;
        y+=dy;
        //ellipse(x, y, d, d);
       	//partes[0].setPosition(x,y);

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
