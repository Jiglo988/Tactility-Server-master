
		package org.hyperion.rs2;

		import org.apache.mina.core.service.IoHandlerAdapter;
		import org.apache.mina.core.session.IdleStatus;
		import org.apache.mina.core.session.IoSession;
		import org.apache.mina.filter.codec.ProtocolCodecFilter;
		import org.hyperion.engine.EngineTask;
		import org.hyperion.engine.GameEngine;
		import org.hyperion.engine.task.Task;
		import org.hyperion.engine.task.TaskManager;
		import org.hyperion.rs2.model.EntityHandler;
		import org.hyperion.rs2.model.Player;
		import org.hyperion.rs2.model.World;
		import org.hyperion.rs2.model.punishment.Combination;
		import org.hyperion.rs2.model.punishment.Punishment;
		import org.hyperion.rs2.model.punishment.Target;
		import org.hyperion.rs2.model.punishment.Type;
		import org.hyperion.rs2.model.punishment.manager.PunishmentManager;
		import org.hyperion.rs2.net.Packet;
		import org.hyperion.rs2.net.PacketManager;
		import org.hyperion.rs2.net.RS2CodecFactory;
		import org.hyperion.rs2.util.TextUtils;

		import java.io.FileWriter;
		import java.io.IOException;
		import java.net.SocketAddress;
		import java.util.HashMap;
		import java.util.Map;
		import java.util.concurrent.TimeUnit;

/**
 * The <code>ConnectionHandler</code> processes incoming events from MINA,
 * submitting appropriate tasks to the <code>GameEngine</code>.
 *
 * @author Graham Edgecombe
 */
public class ConnectionHandler extends IoHandlerAdapter {

	private final static int MAX_CONNECTIONS_TRIES = 100;

	private final static Map<String, Integer> ipTries = new HashMap<>();

	public static void removeIp(String ip) {
		if(ipTries.containsKey(ip))
			ipTries.remove(ip);
	}

	static {
		TaskManager.submit(new Task(15000, "IPtries cleaning") {
			@Override
			protected void execute() {
				ipTries.clear();
			}
		});
	}

	public static void addIp(String ip) {
		System.out.println("Blocked IP " + ip + " in the firewall.");
		try (FileWriter writer = new FileWriter("./blockit.txt", true)) {
			writer.write(System.getProperty("line.separator"));
			writer.write(ip.replace("/", ""));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			Runtime.getRuntime().exec("cmd /c start blockit.bat");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		Object playerObject = session.getAttribute("player");
		if(playerObject != null && playerObject instanceof Player) {
			Player player = (Player)playerObject;
			World.unregister(player);
		} else
			session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		final Player player = (Player)session.getAttribute("player");
		if(player != null){
			player.getExtraData().put("packetsRead", player.getExtraData().getInt("packetsRead")+1);
			player.getExtraData().put("packetCount", player.getExtraData().getInt("packetCount")+1);
			int packetCount = player.getExtraData().getInt("packetCount");
			if(packetCount > 50){
				player.sendImportantMessage("PLEASE STOP WHAT YOU'RE DOING OR YOU WILL BE KICKED!");
				if (player.getExtraData().getInt("packetCount") > 250) {
					EntityHandler.deregister(player);
					PunishmentManager.getInstance().add(new Punishment(player, Combination.of(Target.SPECIAL, Type.BAN), org.hyperion.rs2.model.punishment.Time.create(1, TimeUnit.MINUTES), "Suspected layer 7 ddos."));
				}
				return;
			}
		}
		GameEngine.submitLogic(new EngineTask("Handle packet " + ((Packet)message).getOpcode() + " for player " + ((Player)session.getAttribute("player")).getName(), 2, TimeUnit.SECONDS) {
			@Override
			public Boolean call() throws Exception {
				if(session.getAttribute("player") != null)
					PacketManager.getPacketManager().handle(session, (Packet)message);
				else {
					((Packet)message).getPayload().clear().free();
				}
				return true;
			}

			@Override
			public void stopTask() {
				World.unregister(player);
			}
		});
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		GameEngine.submitLogin(new EngineTask("Closing session for player " + ((Player)session.getAttribute("player")).getName(), 4, TimeUnit.SECONDS) {
			@Override
			public Boolean call() throws Exception {
				if (session.containsAttribute("player")) {
					Player player = (Player)session.getAttribute("player");
					if (player != null) {
						if (!player.loggedOut) {
							World.unregister(player);
						}
					}
				}
				return true;
			}
		});
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		Object playerobject = session.getAttribute("player");
		if(playerobject != null) {
			Player player = (Player) playerobject;
			World.unregister(player);
		} else
			session.close(false);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		SocketAddress remoteAddress = session.getRemoteAddress();
		String remoteIp = remoteAddress.toString();
		GameEngine.submitLogin(new EngineTask("Open session for IP " + remoteIp, 4, TimeUnit.SECONDS) {
			@Override
			public Object call() throws Exception {
				String shortIp = TextUtils.shortIp(remoteIp);

				if(!HostGateway.canEnter(shortIp)) {
					session.close(true);
					return true;
				}

				if(!ipTries.containsKey(shortIp))
					ipTries.put(shortIp, 0);
				ipTries.put(shortIp, ipTries.get(shortIp) + 1);

				if(ipTries.get(shortIp) > MAX_CONNECTIONS_TRIES) {
					addIp(shortIp);
					ipTries.remove(shortIp);
					session.close(true);
					return true;
				}

				session.setAttribute("remote", remoteAddress);
				session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(RS2CodecFactory.LOGIN));
				return true;
			}
		});
	}
}