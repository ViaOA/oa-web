package test.xice.tsac.delegate.oa;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.viaoa.util.OAString;

import test.xice.tsac.model.oa.MRADClient;
import test.xice.tsac.model.oa.MRADClientCommand;
import test.xice.tsac.model.oa.SSHExecute;

public class SSHExecuteDelegate {

	private static Logger LOG = Logger.getLogger(SSHExecuteDelegate.class.getName());

	private static final String USERNAME = "helpdesk";
	private static final String PASSWORD = "password";
	private static final String sshKeyFileName = "/home/helpdesk/.ssh/id_rsa";

	private static boolean bFileExists;
	private static AtomicInteger aiCnt = new AtomicInteger(1);

	/**
	 * Use SSH to run a command for an MRADClient.
	 */
	public static SSHExecute runCommand(final MRADClientCommand mradClientCommand, final String command) throws Exception {
		if (mradClientCommand == null || OAString.isEmpty(command)) {
			return null;
		}
		MRADClient client = mradClientCommand.getMRADClient();
		if (client == null) {
			return null;
		}

		final int id = aiCnt.getAndIncrement();

		final SSHExecute sshExecute = new SSHExecute();
		return sshExecute;
	}

}
