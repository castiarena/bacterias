class Comida {
	FWorld mundo;
	FBody centro;
	FBody comida;
	String nombre;
	int d = 6;
	color c = color(0,208,140);
	color cStroke = color(0,125,0);
	int borde = 2;
	int energia = int(random(55,100));
	float x = 0.0;
	float y = 0.0;
	PVector position;

	Comida (FWorld _m) {
		mundo = _m;
		nombre = "comida";
		x = random(50, width-50);		
		y = random(50, height-50);
		crearComida();
	}

	void crearComida(){
		centro = new FCircle(d);
	 	centro.setPosition(x,y);
	 	centro.setFill(red(c),green(c),blue(c));
	 	centro.setStroke(borde);
		centro.setStrokeColor(cStroke);
	 	mundo.add(centro);

		for (int i = 0; i < energia; ++i) {
			d =int(random(4,8));
			comida = new FCircle(d);
			comida.setFill(red(c),green(c),blue(c));;
			comida.setName(nombre);
			comida.setPosition(x,y);
			comida.setStroke(borde);
			comida.setStrokeColor(cStroke);
			mundo.add(comida);

			FDistanceJoint junta = new FDistanceJoint(centro, comida);
		    junta.setLength(2);
		    junta.setNoStroke();
		    junta.setStroke(0);
		    junta.setFill(0);
		    junta.setDrawable(false);
		    junta.setFrequency(0.8);
		    mundo.add(junta);	
		    
		}
		centro.addForce(40,40);
		
	}

	void actPos(){
		x = centro.getX();
		y = centro.getY();
	}

	PVector pos(){
		actPos();
		position = new PVector(x,y);
		return position;
	}

	int darEnergia(){
		int _e = energia;

		return _e;
	}

}