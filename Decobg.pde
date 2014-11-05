class Decobg extends Coco {
	PImage mancha;
	int i = int(random(1,3));
	Decobg (FWorld _m) {
		super(_m,80);
		selectImage();
		super.addImage(mancha);
	}

	void selectImage(){
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