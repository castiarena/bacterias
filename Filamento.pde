class Filamento {
	float x = 0.0;
	float y = 0.0;
	float dir = 0.0;
	float aceleracionX = 0.0;
	float aceleracionY = 0.0;
	color c = color(191,72,83);;
	FWorld m;
	FBody[] partes = new FBody[10];	
	float d ;
	float frequency = 30;
	float damping = 1;

	float offset = 10;
	float vAng = radians(PI);
	Boolean vive = true;
  	float nDir;
  	String name;
  	int energia= 0;
  	boolean buscaComida = true;

	Filamento (FWorld _m, float _d,int _id) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9;
		aceleracionY = 0.9;
		name = "filamento_"+_id;	
		crearFilamento();
	}

	Filamento (FWorld _m, float _d) {
		m = _m;
		x = random(offset*2 ,width - offset*2);
		y = random(offset*2, height- offset*2);
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9;
		aceleracionY = 0.9;
		name = "vampiro";	
		crearFilamento();
	}

	Filamento (FWorld _m, float _d, PVector  _pos) {
		m = _m;
		x = _pos.x;
		y = _pos.y;
		dir = random(TWO_PI);
		nDir = dir;
		d = _d;
		aceleracionX = 0.9;
		aceleracionY = 0.9;
		name = "vampiro";	
		crearFilamento();
	}


	/*-LOMBRIS-*/
	void crearFilamento(){ //NOMBRES para todos iguales!!!
		for (int i=0; i<partes.length; i++) {
		    partes[i] = new FCircle(d);
		    partes[i].setPosition(x, y);
		    partes[i].setNoStroke();
		    partes[i].setStrokeWeight(0);
		    partes[i].setFill(red(c),green(c),blue(c));
		    partes[i].setGroupIndex(1);
		    partes[i].setDensity(aceleracionX);
		    partes[i].setName(name);
		    m.add(partes[i]);
	  	}

		for (int i=1; i<partes.length; i++) {
		    FDistanceJoint junta = new FDistanceJoint(partes[i-1], partes[i]);
		    junta.setAnchor1(-d/2, 0);
		    junta.setAnchor2(d/2, 0);
		    junta.setStrokeWeight(d);
		    junta.setStrokeColor(c);
		    junta.setFrequency(frequency);
		    junta.setDamping(damping);
		    junta.setFill(red(c),green(c),blue(c));
		    junta.setLength(0);
		    m.add(junta);
		    //trabajar con attachImage y jugar con el tamaño de cada objeto
	  	}
	}


	void mover(ArrayList<Filamento> _f){
		energia -- ;
		if(energia<0){
			_f.remove(this);
		}
		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 

	    if(dist(width/2,height/2,x,y) > height/2){
	    	aceleracionX = aceleracionX*-1;
	    	aceleracionY = aceleracionY*-1;	
	    }

		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);



        x+=dx;
        y+=dy;
        //ellipse(x, y, d, d);

       /*	PVector posCabeza = new PVector(partes[0].getX(),partes[0].getY());
       	PVector posCola = new PVector(partes[partes.length-1].getX(),partes[partes.length-1].getY());
       	if(dist(posCabeza.x, posCabeza.y,posCola.x,posCabeza.y) > partes.length*d){
   			for (int i=0; i<partes.length; i++) {
   				partes[i].setFill(red(c),green(c),blue(c),50);
   				vive = false;
   			}
       	}else{*/
       		if(buscaComida){
       			partes[0].setPosition(x,y);
       		}else{
       			partes[0].addImpulse(dx*0.2,dy*0.2);
	        }
       	/*}*/

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

	void sigueVivo(){
		if(!vive){
			for (int i=0; i<partes.length; i++) {
				m.remove(partes[i]);
			}
		}
	}

	void matar(){
		for (int i=0; i<partes.length; i++) {
			ArrayList<FDistanceJoint> juntas;
			juntas = partes[i].getJoints();
			for (int j = 0; j < juntas.size(); ++j) {
				m.remove(juntas.get(j));
			}			
		}
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

			if(dist(partes[0].getX(),partes[0].getY(),_etx,_ety)< dist(partes[0].getX(),partes[0].getY(),_otx,_oty)){
				x += (_etx-x)*0.05;
				y += (_ety-y)*0.05;
				_tx = _etx;
				_ty = _ety;		
				if(dist(partes[0].getX(),partes[0].getY(),_tx,_ty)< d +10){					
					energia += estaComida.darEnergia(_comida);
				}		
			}else{
				x += (_otx-x)*0.05;
				y += (_oty-y)*0.05;
				_tx = _otx;
				_ty = _oty;
				if(dist(partes[0].getX(),partes[0].getY(),_tx,_ty)< d +10){					
					energia += otraComida.darEnergia(_comida);
				}
			}	

			
		}else{
			buscaComida = false;
		}	
	}
}