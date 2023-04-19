package de.gothaer.gui.presenter;


import de.gothaer.gui.Euro2DollarRechnerView;
import de.gothaer.model.Euro2DollarRechner;

public class Euro2DollarPresenterImpl implements Euro2DollarPresenter {
	
	private Euro2DollarRechnerView view;
	private Euro2DollarRechner model;
	
	
	
	/* (non-Javadoc)
	 * @see de.gui.presenter.IEuro2DollarPresenter#getView()
	 */
	@Override
	public Euro2DollarRechnerView getView() {
		return view;
	}

	/* (non-Javadoc)
	 * @see de.gui.presenter.IEuro2DollarPresenter#setView(de.gui.IEuro2DollarRechnerView)
	 */
	@Override
	public void setView(Euro2DollarRechnerView view) {
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see de.gui.presenter.IEuro2DollarPresenter#getModel()
	 */
	@Override
	public Euro2DollarRechner getModel() {
		return model;
	}

	/* (non-Javadoc)
	 * @see de.gui.presenter.IEuro2DollarPresenter#setModel(de.model.IEuro2DollarRechner)
	 */
	@Override
	public void setModel(Euro2DollarRechner model) {
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see de.gui.presenter.IEuro2DollarPresenter#rechnen()
	 */

	/*
		Eurowert aus der Maske holen
		Eurowert in Double wandeln
		(wenn null -> schreiben Fehlermeldung in das Dollarfeld)
		(wenn NAN -> schreiben Fehlermeldung in das Dollarfeld)
		eurowert an Model übergeben
		Irgendwelche Exceptions -> Fehlermeldung im Dollarfeld
		Ergebnis in String wandeln und ins Dollarfeld schreiben

	 */
	@Override
	public void onRechnen() {
		try {
			double euro = Double.valueOf(view.getEuro());
			double dollar = getModel().calculateEuro2Dollar(euro);
			view.setDollar(String.format("%.2f", dollar));
		} catch (NullPointerException e) {
			view.setDollar("Euro darf nicht null sein");
		} catch (NumberFormatException e) {
			view.setDollar("Keine Zahl");
		} catch (RuntimeException e) {
			view.setDollar("Es ist ein Fehler aufgetreten");
		}


	}
	
	/* (non-Javadoc)
	 * @see de.gui.presenter.IEuro2DollarPresenter#beenden()
	 */
	@Override
	public void onBeenden() {  
		getView().close();
	}
	
	/* (non-Javadoc)
	 * @see de.gui.presenter.IEuro2DollarPresenter#populateItems()
	 */
	@Override
	public void onPopulateItems() {
		view.setEuro("0");
		view.setDollar("0");
		view.setRechnenEnabled(true);
	}

	@Override
	public void updateRechnenActionState() {


	}

}
