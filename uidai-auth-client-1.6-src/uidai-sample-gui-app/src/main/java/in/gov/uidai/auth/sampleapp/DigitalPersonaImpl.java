/**
 * 
 */
package in.gov.uidai.auth.sampleapp;


import in.gov.uidai.auth.client.biometrics.*;
import in.gov.uidai.auth.device.model.DeviceCollectedAuthData;
import in.gov.uidai.authentication.uid_auth_request_data._1.BioMetricType;
import in.gov.uidai.authentication.uid_auth_request_data._1.BiometricPosition;
/**
 * @author kumarde
 *
 */
public class DigitalPersonaImpl implements BiometricIntegrationAPI {

	@Override
	public void captureBiometrics(CaptureHandler callback) {
	}
}
