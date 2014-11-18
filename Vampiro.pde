class Vampiro{
	float x, y;
	float dir = 0.0;
	float nDir;
	color c = color(0,72,110);
	PImage img;
	FBody cuerpo;
	float vAng = radians(PI);
	float offset = 10;
	int colas = 4;
	FBody[][] partes = new FBody[colas][5];
	FWorld mundo;

	int ancho = 100;
	int alto = 30;
	int d = 2;
	float aceleracionX;
	float aceleracionY;
	boolean buscaComida = false;

	int energia = 100;

	Vampiro (FWorld _m, PVector pos) {
		mundo = _m;
		x = pos.x;
		y = pos.y;

		aceleracionX = 0.9;
		aceleracionY = 0.9;
		img = loadImage("data/vampiro.png");
		crearVampiro();
	}

	void dibujar(){
		pushStyle();
		pushMatrix();
			float escala = map(ancho,50,100,0.4,1);
			translate(cuerpo.getX(), cuerpo.getY());
			//ellipse(0,0,d,d);
			scale(escala);
			imageMode(CENTER);
			image(img,0,0);
		popMatrix();
		popStyle();
	}

	void crearVampiro(){
		cuerpo = new FBox(ancho, alto);
		cuerpo.setPosition(x,y);
		cuerpo.setNoStroke();
		cuerpo.setNoFill();
		cuerpo.attachImage(img);
		mundo.add(cuerpo);

		for (int j = 0; j < colas; ++j) {

			for (int i=0; i<partes.length; i++) {
			    partes[j][i] = new FCircle(d);
			    partes[j][i].setPosition(x, y);
			    partes[j][i].setNoStroke();
			    partes[j][i].setStrokeWeight(0);
			    partes[j][i].setFill(red(c),green(c),blue(c));
			    partes[j][i].setGroupIndex(1);
			    mundo.add(partes[j][i]);
		  	}

			for (int i=1; i<partes.length; i++) {

			    FDistanceJoint junta = new FDistanceJoint(partes[j][i-1], partes[j][i]);
			    junta.setAnchor1(-d/2, 0);
			    junta.setAnchor2(d/2, 0);
			    junta.setStrokeWeight(d);
			    junta.setStrokeColor(c);
			    junta.setFrequency(10);
			    junta.setDamping(1);
			    junta.setFill(red(c),green(c),blue(c));
			    junta.setLength(0);
			    mundo.add(junta);
			    //trabajar con attachImage y jugar con el tamaño de cada objeto
		  	}

			FDistanceJoint juntar = new FDistanceJoint(cuerpo, partes[j][0]);
			juntar.setAnchor1(ancho-40, 0);
		    juntar.setAnchor2(d/2, 0);
			juntar.setStrokeWeight(d);
		    juntar.setStrokeColor(c);
		    juntar.setFrequency(0);
		    juntar.setDamping(1);
			mundo.add(juntar);
		}
		

	}

	void mover(ArrayList<Vampiro> _v){
		energia -- ;
		if(energia<0){
			_v.remove(this);
		}
		float diferencia = menorDistAngulos( dir, nDir );
	    float f = 0.05;

	    dir += diferencia * f;
	    dir += random( -vAng, vAng ); 


	 	/*aceleracionX =  x > width - offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY =  y > height - offset ? aceleracionY*-1 : aceleracionY ;
	 	aceleracionX =  x < offset ? aceleracionX*-1 : aceleracionX ;
	 	aceleracionY =  y < offset ? aceleracionY*-1 : aceleracionY ;*/
	 	if(dist(width/2,height/2,x,y) > height/2){
	    	aceleracionX = aceleracionX*-1;
	    	aceleracionY = aceleracionY*-1;	
	    }
		float dx = aceleracionX * sin( dir);
		float dy = aceleracionY * cos( dir);

		cuerpo.addTorque(dx*50);


        x+=dx;
        y+=dy;
       		if(buscaComida){
       			cuerpo.setPosition(x,y);
       		}else{
       			cuerpo.addImpulse(dx*10,dy*10);
	        }

	}

	void comer(ArrayList<Coco> cocos){
		
		for (int i = cocos.size()-1; i >= 0; i--) {
			Coco thisCoco = cocos.get(i);
			PVector pos = thisCoco.getPos();
			if(dist(x,y,pos.x,pos.y)<d){
				mundo.remove(thisCoco.cuerpo());
			}
		}
	}
}