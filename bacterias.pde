import fisica.*;

FWorld mundo;
FWorld bg;
ArrayList<Decobg> decos;


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

	bg = new FWorld();
	bg.setGravity(0,0);
	bg.setEdges(color(255,255,255,0));


	decos = new ArrayList<Decobg>();
	for (int i = 0; i < 8; i++) {
		decos.add(new Decobg(bg));
	}
}

void draw() {
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
		esteCoco.buscarComida(comidas);
	}

	mundo.step();
	mundo.draw();
	

	
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
	}
}

