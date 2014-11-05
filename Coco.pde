class Coco {
	boolean coco = true;

	float x = 0.0;
	float y = 0.0;
	float dir = 0.0;
	float aceleracionX = 0.0;
	float aceleracionY = 0.0;
	color c = color(233,161,76);
		
	float d ;
	float frequency = 10;
	float damping = 0.1;

	float offset = 10;
	float vAng = radians(PI);

  	float nDir;
  	String nombre;
	FWorld m;
	FBody cuerpo;
  	Boolean vive = true;

  	PImage pelos;
  	int energia = 10;

	Coco (FWorld _m, float _d, int _id) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9;
		aceleracionY = 0.9;
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
		aceleracionX = 0.9;
		aceleracionY = 0.9;
		nombre = "coco";
		pelos = loadImage("data/bola.png");

		crearCoco();
	}



	/*-COCO-*/
	void crearCoco(){
	    cuerpo = new FCircle(d);
	    cuerpo.setPosition(x, y);
	    cuerpo.setNoStroke();
	    cuerpo.setNoFill();
	    cuerpo.setDensity(d/100);
	    cuerpo.setName(nombre);
	    //cuerpo.attachImage(pelos);
	    m.add(cuerpo);

	}

	void dibujar(){
		pushStyle();
		pushMatrix();
			float escala = map(d,6,80,0.4,1);
			translate(cuerpo.getX(), cuerpo.getY());
			ellipse(0,0,d,d);
			scale(escala);
			imageMode(CENTER);
			image(pelos,0,0);
		popMatrix();
		popStyle();
	}

	void mover(){
		if(coco){
			dibujar();
		}

		

		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 


	 	aceleracionX =  x > width - offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = 	y > height - offset ? aceleracionY*-1 : aceleracionY ;
	 	aceleracionX =  x < offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = 	y < offset ? aceleracionY*-1 : aceleracionY ;

		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);

		cuerpo.addTorque(dx*5);

       	//cuerpo.addForce(dx*(energia*5),dy*(energia)*5);  //buscar la magnitud valor 
        x+=dx;
        y+=dy;
       	cuerpo.setPosition(x,y);

	}

	void setPos(float _x, float _y){
		x= _x;
		y=_y;

		cuerpo.setPosition(x,y);
	}

	void changeColor(color _c){
		c = _c;
	}

	void changeRadio(float _d){
		d = _d;
	}

	float diam(){
		return d;
	}


	void matar(){
		m.remove(cuerpo);
	}
	void crecer(){
		d+= energia/35;
	}
	void buscarComida(ArrayList<Comida> _comida){
		if(energia<100){
		println("energia: "+energia);	
			float _tx = 0.0;
			float _ty = 0.0;

			Comida estaComida = _comida.get(_comida.size()-1);
			Comida otraComida = _comida.get(0);

			float _etx = estaComida.pos().x;
			float _ety = estaComida.pos().y;

			float _otx = otraComida.pos().x;
			float _oty = otraComida.pos().y;

			if(dist(cuerpo.getX(),cuerpo.getY(),_etx,_ety)< dist(cuerpo.getX(),cuerpo.getY(),_otx,_oty)){
				x += (_etx-x)*0.09;
				y += (_ety-y)*0.09;
				_tx = _etx;
				_ty = _ety;		
				if(dist(cuerpo.getX(),cuerpo.getY(),_tx,_ty)< d -5){					
					energia += estaComida.darEnergia(_comida);
					crecer();
				}		
			}else{
				x += (_otx-x)*0.09;
				y += (_oty-y)*0.09;
				_tx = _otx;
				_ty = _oty;
				if(dist(cuerpo.getX(),cuerpo.getY(),_tx,_ty)< d -5){					
					energia += otraComida.darEnergia(_comida);
					crecer();
				}
			}		

			
		}				

		
	}

	void addImage(PImage _i){
		energia = 100;
		coco = false;
		cuerpo.setNoStroke();
		cuerpo.setNoFill();
		cuerpo.attachImage(_i);
	}
}