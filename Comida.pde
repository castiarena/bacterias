class Comida {
	FWorld mundo;
	FBody centro;
	FBody comida;
	String nombre;
	int d = 6;
	color c = color(0,208,140);
	color cStroke = color(0,125,0);

	color c2 = color(73,0,34);

	int borde = 2;
	int energia = 5;
	int energiaValor = 100;
	float x = 0.0;
	float y = 0.0;
	PVector position;

	ArrayList<FCircle> comidas;

	Comida (FWorld _m) {
		mundo = _m;
		comidas = new ArrayList<FCircle>();
		nombre = "comida";
		x = random(50, width-50);		
		y = random(50, height-50);
		crearComida(true);
	}
	Comida (FWorld _m, PVector _pos) {
		mundo = _m;
		comidas = new ArrayList<FCircle>();
		nombre = "comida";
		x = _pos.x;		
		y =_pos.y;
		crearComida(true);
	}


	Comida (FWorld _m , float _x,float _y) {
		mundo = _m;
		comidas = new ArrayList<FCircle>();
		nombre = "desecho";
		x =_x;		
		y =_y;
		c= c2;
		crearComida(false);
	}

	void crearComida(boolean _borde){
		centro = new FCircle(d);
	 	centro.setPosition(x,y);
	 	centro.setFill(red(c),green(c),blue(c));
	 	if(_borde){
	 		centro.setStroke(borde);
			centro.setStrokeColor(cStroke);
	 	}else{
	 		centro.setNoStroke();
	 	}
	 	mundo.add(centro);

		for (int i = 0; i < energia; ++i) {
			d =int(random(4,8));
			comidas.add(new FCircle(d));		    
		}

		for (int i = comidas.size()-1; i >= 0; i--){
			FCircle tComida = comidas.get(i);
			tComida.setFill(red(c),green(c),blue(c));;
			tComida.setName(nombre);
			tComida.setPosition(x,y);
			if(_borde){		 		
				tComida.setStroke(borde);
				tComida.setStrokeColor(cStroke);
		 	}else{
		 		tComida.setNoStroke();
		 	}
			mundo.add(tComida);

			FDistanceJoint junta = new FDistanceJoint(centro, tComida);
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

	int darEnergia(ArrayList _donde){
		int _e = energiaValor;

		if(energiaValor < 1){
			for (int i = comidas.size()-1; i >= 0; i--){
				mundo.remove(comidas.get(i));
			}
			mundo.remove(centro);
			_donde.remove(this);
		}else{
			energiaValor--;
			FCircle estaComida = comidas.get(int(random(comidas.size())));
			mundo.remove(estaComida);
		}
		
		return _e;
	}

	int tam(){
		return energia * d;
	}
}