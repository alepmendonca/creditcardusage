package br.com.alepmendonca.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;

import br.com.alepmendonca.mail.bradesco.BradescoMailNotifiable;

public class CardReceipt implements BradescoMailNotifiable {

	private long id;
	private Store estabelecimento;
	private CreditCard cartao;
	private BigDecimal valor;
	private Currency moeda;
	private Calendar dataHoraAutorizacao;
	private long autenticacao;

	public CardReceipt(long _id, Store _estabelecimento, CreditCard _cartao,
			BigDecimal _valor, Currency _moeda,
			Calendar _dataHoraAutorizacao, long _autenticacao) {
		super();
		setId(_id);
		setEstabelecimento(_estabelecimento);
		setCartao(_cartao);
		setValor(_valor);
		setMoeda(_moeda);
		setDataHoraAutorizacao(_dataHoraAutorizacao);
		setAutenticacao(_autenticacao);
	}
	
	@Override
	public String toString() {
		return "Cartão final " + getCartao() + "\nEstabelecimento: " + getEstabelecimento() + 
				"\nValor: " + NumberFormat.getCurrencyInstance().format(getValor()) + 
				"\nData e Hora: " + DateFormat.getDateTimeInstance().format(getDataHoraAutorizacao().getTime()) +
				"\nNúmero Autenticação: " + getAutenticacao();
				
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public Store getEstabelecimento() {
		return estabelecimento;
	}

	private void setEstabelecimento(Store estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public CreditCard getCartao() {
		return cartao;
	}

	private void setCartao(CreditCard cartao) {
		this.cartao = cartao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	private void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Currency getMoeda() {
		return moeda;
	}

	private void setMoeda(Currency moeda) {
		this.moeda = moeda;
	}

	public Calendar getDataHoraAutorizacao() {
		return dataHoraAutorizacao;
	}

	private void setDataHoraAutorizacao(Calendar dataHoraAutorizacao) {
		this.dataHoraAutorizacao = dataHoraAutorizacao;
	}
	
	public long getAutenticacao() {
		return autenticacao;
	}

	private void setAutenticacao(long autenticacao) {
		this.autenticacao = autenticacao;
	}

	@Override
	public void notifyBradescoMailArrival() {
		// TODO Auto-generated method stub
	}
}
