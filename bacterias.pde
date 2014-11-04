import fisica.*;

FWorld mundo;
ArrayList<Filamento> filamentos;
ArrayList<Coco> cocos;
ArrayList<Comida> comidas;
color colorFilamentos = color(191,72,83);
color colorCocos = color(233,161,76);

void setup() {
	size(800, 600);
	smooth();

	Fisica.init(this);

	mundo = new FWorld();
	mundo.setEdges(color(255,255,255,0));
	mundo.setGravity(0,0);

	filamentos = new ArrayList<Filamento>(); 
	cocos = new ArrayList<Coco>();
	comidas = new ArrayList<Comida>(); 

}

void draw() {
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

void keyPressed(){

	switch (key) {
		case 'f' :
			filamentos.add(new Filamento(mundo,colorFilamentos,5,filamentos.size()));
		break;	
		case 'F' :
			int cf = int(random(filamentos.size()));
			Filamento esteFilamento= filamentos.get(cf);
			esteFilamento.matar();
		break;	
		case 'c' :
			cocos.add(new Coco(mundo,colorCocos,50, cocos.size()));			
		break;	
		case 'C' :
			int cc = int(random(cocos.size()));
			Coco esteCoco= cocos.get(cc);
			esteCoco.matar();
		break;	
		case 'm' :
			comidas.add(new Comida(mundo));
		break;	
	}
}

