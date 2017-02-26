package org.hyperion.rs2.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;

public class VoteSystem {

	public static LinkedList<VoteObject> votes = new LinkedList<VoteObject>();

	public static void init() {
		try {
			System.out.println("Loading vote objects!");
			BufferedReader br = new BufferedReader(new FileReader(
					"./data/voteobjects.txt"));
			String s = "";
			while((s = br.readLine()) != null) {
				String parts[] = s.split(",");
				long time = Long.parseLong(parts[2]);
				VoteObject vo = new VoteObject(parts[0], parts[1], time);
				if(! vo.canVote()) {
					votes.add(vo);
				}
			}
			br.close();
			System.out.println("Loaded : " + votes.size() + " vote objects!");
			BufferedWriter bw = new BufferedWriter(new FileWriter("./data/voteobjects.txt", false));
			for(VoteObject vo : votes) {
				bw.write(vo.getName() + "," + vo.getIP() + "," + vo.getTime());
				bw.newLine();
			}
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
