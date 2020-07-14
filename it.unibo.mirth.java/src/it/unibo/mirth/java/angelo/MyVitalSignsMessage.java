package it.unibo.mirth.java.angelo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unused")
public class MyVitalSignsMessage {
	private String timestamp;
	private String message;
	private String deviceId;
	
	private String patientId;
	private String patientName;
	
	private List<VitalSign> observations;
	
	private MyVitalSignsMessage(String timestamp, String message, String deviceId, String patientId, String patientName, List<VitalSign> observations) {
		this.timestamp = timestamp;
		this.message = message;
		this.deviceId = deviceId;
		
		this.patientId = patientId;
		this.patientName = patientName;
		
		this.observations = observations;
	}
	
	public String toJsonRep() {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		
		return gson.toJson(this);
	}

	public static class Builder {
		private String timestamp;
		private String message;
		private String deviceId;
		
		private String patientId;
		private String patientName;
		
		private List<VitalSign> observations = new ArrayList<MyVitalSignsMessage.VitalSign>();
		
		Builder timestamp(String timestamp) {
			this.timestamp = timestamp;
			return this;
		}
		
		Builder message(String message) {
			this.message = message;
			return this;
		}
		
		Builder deviceId(String deviceId) {
			this.deviceId = deviceId;
			return this;
		}
		
		Builder patientId(String patientId) {
			this.patientId = patientId;
			return this;
		}
		
		Builder patientName(String patientName) {
			this.patientName = patientName;
			return this;
		}
		
		Builder addObservation(VitalSign vs) {
			observations.add(vs);
			return this;
		}
		
		MyVitalSignsMessage build() {
			return new MyVitalSignsMessage(timestamp, message, deviceId, patientId, patientName, observations);
		}
	}
	
	static class VitalSign {
		private String name;
		private String value;
		private String uom;
		private String timestamp;
		
		public VitalSign(String name, String value, String uom, String timestamp) {
			this.name = name;
			this.value = value;
			this.uom = uom;
			this.timestamp = timestamp;
		}
	}
}
