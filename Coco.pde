class Coco {
	boolean coco = true;
	boolean buscaComida = true;
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

  	int timer;
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

	Coco (FWorld _m, float _d, PVector _pos) {
		m = _m;
		x = _pos.x;
		y = _pos.y;
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
			//ellipse(0,0,d,d);
			scale(escala);
			imageMode(CENTER);
			image(pelos,0,0);
		popMatrix();
		popStyle();
	}

	void mover(ArrayList<Coco> _c){
		timer = millis();

		if(coco){
			dibujar();
		}

		if(timer%101 == 100){
			energia-=5;
		}

		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 


	 	/*aceleracionX =  x > width - offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = 	y > height - offset ? aceleracionY*-1 : aceleracionY ;
	 	aceleracionX =  x < offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY = 	y < offset ? aceleracionY*-1 : aceleracionY ;*/
	 	if(dist(width/2,height/2,x,y) > height/2){
	    	aceleracionX = aceleracionX*-1;
	    	aceleracionY = aceleracionY*-1;	
	    }

		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);

		cuerpo.addTorque(dx*5);
		float xa = cuerpo.getX();
		float ya = cuerpo.getY();
        x+=dx;
        y+=dy;
        if(buscaComida || !coco){
        	xa+=(x-xa)*0.2;
        	ya+=(y-ya)*0.2;
       		cuerpo.setPosition(xa,ya);
        }else{
       		cuerpo.addForce(dx*(energia*0.2),dy*(energia*0.2));  //buscar la magnitud valor
       		energia -- ;
       		if(energia<0){
				_c.remove(this);
			} 
        }

	}

	void setPos(float _x, float _y){
		x= _x;
		y=_y;

		cuerpo.setPosition(x,y);
	}

	void seguir(float _xpos,float _ypos){
		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 

		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);

		cuerpo.addForce(dx*(energia*0.2),dy*(energia*0.2));
	}

	PVector getPos(){
		PVector salida = new PVector(cuerpo.getX(),cuerpo.getY());
		return salida;
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
		if(energia<100){
			buscaComida = true;	
			float _tx = 0.0;
			float _ty = 0.0;

			Comida estaComida = _comida.get(_comida.size()-1);
			Comida otraComida = _comida.get(0);

			float _etx = estaComida.pos().x;
			float _ety = estaComida.pos().y;

			float _otx = otraComida.pos().x;
			float _oty = otraComida.pos().y;

			if(dist(cuerpo.getX(),cuerpo.getY(),_etx,_ety)< dist(cuerpo.getX(),cuerpo.getY(),_otx,_oty)){
				x += (_etx-x)*0.05;
				y += (_ety-y)*0.05;
				_tx = _etx;
				_ty = _ety;		
				if(dist(cuerpo.getX(),cuerpo.getY(),_tx,_ty)< d +10){					
					energia += estaComida.darEnergia(_comida);
				}		
			}else{
				x += (_otx-x)*0.05;
				y += (_oty-y)*0.05;
				_tx = _otx;
				_ty = _oty;
				if(dist(cuerpo.getX(),cuerpo.getY(),_tx,_ty)< d +10){					
					energia += otraComida.darEnergia(_comida);
				}
			}	

			
		}else{
			buscaComida = false;
		}			

		
	}

	void addImage(PImage _i){
		energia = 100;
		coco = false;
		cuerpo.setNoStroke();
		cuerpo.setNoFill();
		cuerpo.attachImage(_i);
	}


	void desecho(ArrayList<Comida> _desecho){
		if(!buscaComida && _desecho.size()<10){
			energia-=80;
			_desecho.add(new Comida(mundo,cuerpo.getX()-d,cuerpo.getY()-d));
		}
	}

	FBody cuerpo(){
		return cuerpo;
	}
}