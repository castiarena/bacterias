class Comida {
	FWorld mundo;
	FBody comida;
	String nombre;
	int d = 6;
	color c = color(0,208,140);
	color cStroke = color(0,125,0);
	int energia = int(random(5,25));

	Comida (FWorld _m) {
		mundo = _m;
		nombre = "comida";
		crearComida();
	}

	void crearComida(){
		FBlob liquido = new FBlob();

		 liquido.setPosition(width/2,height/2);
		 liquido.setAsCircle(540,40);
		 liquido.setFill(red(c),green(c),blue(c));
		 mundo.add(liquido);

		for (int i = 0; i < energia; ++i) {
			comida = new FCircle(d);
			comida.setFill(red(c),green(c),blue(c));;
			comida.setName(nombre);
			comida.setPosition(width/2,height/2);
			comida.setStroke(1);
			comida.setStrokeColor(cStroke);
			mundo.add(comida);	
		}
		
	}

}