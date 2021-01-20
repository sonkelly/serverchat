/**
 * CardServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package vn.game.cardservice;

@SuppressWarnings("serial")
public class CardServiceLocator extends org.apache.axis.client.Service implements CardService {
	public CardServiceLocator() {
	}

	public CardServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public CardServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for CardServiceHttpSoap11Endpoint
	private java.lang.String CardServiceHttpSoap11Endpoint_address = "http://125.212.192.96:9006/axis2/services/CardService.CardServiceHttpSoap11Endpoint/";

	public java.lang.String getCardServiceHttpSoap11EndpointAddress() {
		return CardServiceHttpSoap11Endpoint_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String CardServiceHttpSoap11EndpointWSDDServiceName = "CardServiceHttpSoap11Endpoint";

	public java.lang.String getCardServiceHttpSoap11EndpointWSDDServiceName() {
		return CardServiceHttpSoap11EndpointWSDDServiceName;
	}

	public void setCardServiceHttpSoap11EndpointWSDDServiceName(java.lang.String name) {
		CardServiceHttpSoap11EndpointWSDDServiceName = name;
	}

	public CardServicePortType getCardServiceHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(CardServiceHttpSoap11Endpoint_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getCardServiceHttpSoap11Endpoint(endpoint);
	}

	public CardServicePortType getCardServiceHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			CardServiceSoap11BindingStub _stub = new CardServiceSoap11BindingStub(portAddress, this);
			_stub.setPortName(getCardServiceHttpSoap11EndpointWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setCardServiceHttpSoap11EndpointEndpointAddress(java.lang.String address) {
		CardServiceHttpSoap11Endpoint_address = address;
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	@SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (CardServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
				CardServiceSoap11BindingStub _stub = new CardServiceSoap11BindingStub(new java.net.URL(CardServiceHttpSoap11Endpoint_address), this);
				_stub.setPortName(getCardServiceHttpSoap11EndpointWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	@SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		if (portName == null) { return getPort(serviceEndpointInterface); }
		java.lang.String inputPortName = portName.getLocalPart();
		if ("CardServiceHttpSoap11Endpoint".equals(inputPortName)) {
			return getCardServiceHttpSoap11Endpoint();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://service.card.tuquy.com", "CardService");
	}

	@SuppressWarnings("rawtypes")
	private java.util.HashSet ports = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://service.card.tuquy.com", "CardServiceHttpSoap11Endpoint"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("CardServiceHttpSoap11Endpoint".equals(portName)) {
			setCardServiceHttpSoap11EndpointEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
