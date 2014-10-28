class Coco {
	float x = 0.0;
	float y = 0.0;
	float dir = 0.0;
	float aceleracionX = 0.0;
	float aceleracionY = 0.0;
	color c;
	FWorld m;
	FBody[] partes = new FBody[10];	
	float d ;
	float frequency = 10;
	float damping = 0.1;

	float offset = 10;
	float vAng = radians(PI);

  	float nDir;

	Coco (FWorld _m, color _c, float _d) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9;
		aceleracionY = 0.9;
		c = _c;		
		crearCoco();
	}



	/*-COCO-*/
	void crearCoco(){
	    partes[0] = new FCircle(d);
	    partes[0].setPosition(x, y);
	    partes[0].setNoStroke();
	    partes[0].setFill(red(c),green(c),blue(c));
	    partes[0].setGroupIndex(1);
	    m.add(partes[0]);

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


       	partes[0].addForce(dx*100,dy*100);  //buscar la magnitud valor bl ablbla abl

        x+=dx;
        y+=dy;
        ellipse(x, y, d, d);
       	//partes[0].setPosition(x,y);

	}

	void setPos(float _x, float _y){
		x= _x;
		y=_y;

		partes[0].setPosition(x,y);
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



}