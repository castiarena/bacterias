class Coco {
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
  	int energia = 0;

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
	    cuerpo.setFill(red(c),green(c),blue(c));
	    cuerpo.setGroupIndex(1);
	    cuerpo.setDensity(d/100);
	    cuerpo.setName(nombre);
	    cuerpo.attachImage(pelos);
	    m.add(cuerpo);

	}


	void mover(){

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

       	cuerpo.addForce(dx*300,dy*300);  //buscar la magnitud valor 
        x+=dx;
        y+=dy;
       	//cuerpo.setPosition(x,y);

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

	void buscarComida(ArrayList<Comida> _comida){
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

	void addImage(PImage _i){
		cuerpo.setNoStroke();
		cuerpo.setNoFill();
		cuerpo.attachImage(_i);
	}
}