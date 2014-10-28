import fisica.*;

FWorld mundo;

Filamento[] filamento = new Filamento[50];
int limiteInicio = 5;
Coco[] cocos = new Coco[50];

void setup() {
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

void draw() {
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

void mousePressed(){
	limiteInicio++;
	filamento[limiteInicio] = new Filamento(mundo,color(196,69,90), 5);
	cocos[limiteInicio] = new Coco(mundo,color(234,162,77), 50);
	filamento[limiteInicio].setPos(mouseX +100,mouseY +100);
	cocos[limiteInicio].setPos(mouseX,mouseY);
}
