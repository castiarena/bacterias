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

	ArrayList<FCircle> comidas;

	Comida (FWorld _m) {
		mundo = _m;
		comidas = new ArrayList<FCircle>();
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
			comidas.add(new FCircle(d));		    
		}

		for (int i = comidas.size()-1; i >= 0; i--){
			FCircle tComida = comidas.get(i);
			tComida.setFill(red(c),green(c),blue(c));;
			tComida.setName(nombre);
			tComida.setPosition(x,y);
			tComida.setStroke(borde);
			tComida.setStrokeColor(cStroke);
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
		int _e = energia;

		if(energia < 1){
			for (int i = comidas.size()-1; i >= 0; i--){
				mundo.remove(comidas.get(i));
			}
			mundo.remove(centro);
			_donde.remove(this);
		}else{
			energia--;
			FCircle estaComida = comidas.get(energia);
			mundo.remove(estaComida);
		}
		
		return int(map(_e,100,0,0,20));
	}

	int tam(){
		return energia * d;
	}
}