package org.hyperion.cache.map;

import org.hyperion.cache.Cache;
import org.hyperion.cache.index.impl.MapIndex;
import org.hyperion.cache.util.ByteBufferUtils;
import org.hyperion.cache.util.ZipUtils;
import org.hyperion.rs2.model.GameObject;
import org.hyperion.rs2.model.GameObjectDefinition;
import org.hyperion.rs2.model.ObjectManager;
import org.hyperion.rs2.model.Position;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A class which parses landscape files and fires events to a listener class.
 *
 * @author Graham Edgecombe
 */
public class LandscapeParser {

	/**
	 * Parses the landscape file.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	public void parse(Cache cache, int area) throws IOException {
		int x = ((area >> 8) & 0xFF) * 64;
		int y = (area & 0xFF) * 64;

		MapIndex index = cache.getIndexTable().getMapIndex(area);

		/*OutputStream os = new FileOutputStream("data/mapdump/objects/"+index.getLandscapeFile()+".rsmap.gz");//dump file
		os.write(cache.getFile(4, index.getLandscapeFile()).getBytes());
		os.flush();
		os.close();
		os = new FileOutputStream("data/mapdump/tiles/"+index.getMapFile()+".rsmap.gz");//dump file
		os.write(cache.getFile(4, index.getMapFile()).getBytes());
		os.flush();
		os.close();*/
		try {
			ByteBuffer buf = ZipUtils.unzip(cache.getFile(4, index.getLandscapeFile()));
			int objId = - 1;
			while(true) {
				int objIdOffset = ByteBufferUtils.getSmart(buf);
				if(objIdOffset == 0) {
					break;
				} else {
					objId += objIdOffset;
					int objPosInfo = 0;
					while(true) {
						int objPosInfoOffset = ByteBufferUtils.getSmart(buf);
						if(objPosInfoOffset == 0) {
							break;
						} else {
							objPosInfo += objPosInfoOffset - 1;

							int localX = objPosInfo >> 6 & 0x3f;
							int localY = objPosInfo & 0x3f;
							int plane = objPosInfo >> 12;

							int objOtherInfo = buf.get() & 0xFF;

							int type = objOtherInfo >> 2;
							int rotation = objOtherInfo & 3;

							Position loc = Position.create(localX + x, localY + y, plane);

							ObjectManager.objectParsed(new GameObject(GameObjectDefinition.forId(objId), loc, type, rotation));
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
