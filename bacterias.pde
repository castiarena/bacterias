import fisica.*;
import TUIO.*;
import processing.opengl.*;
import java.util.*; 

TuioProcessing tuioClient;
FWorld mundo;

int timer;
int cual;

ArrayList<Filamento> filamentos;
ArrayList<Coco> cocos;
ArrayList<Comida> comidas;
ArrayList<Comida> desechos;
ArrayList<Vampiro> vampiros;

color colorFilamentos = color(191,72,83);
color colorCocos = color(233,161,76);

PVector crear;

PImage over;

void setup() {
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

void draw() {

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

void keyPressed(){

	switch (key) {
		case 'f' :
			filamentos.add(new Filamento(mundo,5,filamentos.size()));
		break;	
		case 'F' :
			int cf = int(random(filamentos.size()));
			Filamento esteFilamento= filamentos.get(cf);
			esteFilamento.matar();
		break;	
		case 'c' :
			cocos.add(new Coco(mundo,20, cocos.size()));			
		break;	
		case 'C' :
			int cc = int(random(cocos.size()));
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

void removeTuioCursor(TuioCursor tcur) {
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


void updateTuioCursor(TuioCursor tcur){
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


void crearOrganismo(int _c,PVector _crear){
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