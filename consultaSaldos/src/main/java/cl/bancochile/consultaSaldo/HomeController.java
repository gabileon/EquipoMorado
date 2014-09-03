package cl.bancochile.consultaSaldo;


import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cl.bancochile.osb.TarjetaCreditos.ConsultarSaldos.ConsultarSaldosProxy;
import cl.bancochile.osb.TarjetaCreditos.ConsultarSaldos.ConsultarSaldosRequest;
import cl.bancochile.osb.TarjetaCreditos.ConsultarSaldos.ConsultarSaldosResponse;
import cl.bancochile.osb.TarjetaCreditos.ConsultarSaldos.ConsultarSaldos_PortType;
import cl.bancochile.osb.TarjetaCreditos.ConsultarSaldos.ConsultarSaldos_Service;
import cl.bancochile.osb.TarjetaCreditos.ConsultarSaldosRequest.ReqConsultarSaldosType;
import cl.bancochile.osb.TarjetaCreditos.ConsultarSaldosRequest.CuerpoType;
import cl.bancochile.osb.TarjetaCreditos.ConsultarSaldosResponse.DatosTarjetaAutorizadorType;



/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Resource(name="numeroCuentaTC")
	private ConsultarSaldos_PortType numeroCuentaTC ;
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/home",  method={RequestMethod.POST,RequestMethod.GET})
	
	 public @ResponseBody Map<String,String> home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
			
		
		CuerpoType cuerpoType = new CuerpoType();
		ConsultarSaldosRequest csr = new ConsultarSaldosRequest();
		ReqConsultarSaldosType req = new ReqConsultarSaldosType();
		cuerpoType.setNumeroTarjeta("4152822099999204");
		cuerpoType.setSecuenciaTarjeta(1);
		req.setCuerpo(cuerpoType);
		csr.setReqConsultarSaldos(req);
		ConsultarSaldosResponse consul = new ConsultarSaldosResponse();
		
		
		try {
			((BindingProvider)numeroCuentaTC).getRequestContext().put("numeroTarjeta", "4152822099999204" );
			((BindingProvider)numeroCuentaTC).getRequestContext().put("secuenciaTarjeta", 1);
			consul = numeroCuentaTC.consultarSaldos(csr);
			
			return crearJSON(consul.getRespConsultarSaldos().getCuerpo().getDatosTarjetaAutorizador());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Map<String, String> crearJSON(DatosTarjetaAutorizadorType c)
	{
		Map<String, String>  mapa = new HashMap<String, String>();
		mapa.put("cupoComprasNacional", c.getCupoComprasNacional().toString());

	return mapa;
	}

	
}









