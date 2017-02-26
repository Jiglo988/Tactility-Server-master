package org.hyperion.map;

import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * @author Martin
 */

public class WorldMap {
    public static int c, c1, c2, c3, c4, c5, c6, c7 = 0;
    public static byte[] sizeX, sizeY;
    public static boolean[] hasActions, isClipped;
    public static int highest = 0;

    public static Map<Integer, Object> thiefstalls = new HashMap<Integer, Object>();

    public static Map<Integer, Object> bankBooths = new HashMap<Integer, Object>();


    // loadDatabases() by Phate/WinterLove, renamed & edit by WhiteFang
    public static void loadWorldMap() {
        try {
                new Thread() {
                    @Override
                    public void run() {
                        loadHeights();
                        loadObjectSizes();
                        loadWorldMap2();
                        loadCustomObjs();
                        sizeX = null;
                        sizeY = null;
                        shootable = null;
                        for (int i = 0; i < World.worldmapobjects; i++) {
                            if (World.World_Objects[i].size() <= 0) {
                                World.World_Objects[i] = null;
                            }
                        }
                    }
                }.start();
            if(Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
                Server.getLogger().log(Level.INFO, "Worldmap has been started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getSizeX(int id) {
        //return sizeX[id];
        return GameObjectDefinition.forId(id).getSizeX();
    }

    private static int getSizeY(int id) {
        //return sizeY[id];
        return GameObjectDefinition.forId(id).getSizeY();
    }

    private static void loadCustomObjs() {
        blockSquare(2917, 5316, 2, 2937, 5330);
        blockSquare(2888, 5258, 0, 2908, 5276);
        blockSquare(2824, 5295, 2, 2842, 5309);
        blockSquare(2863, 5351, 2, 2877, 5369);
    }

    public static void blockSquare(int x, int y, int height, int finishX, int finishY) {
        ArrayList<Integer> directions = new ArrayList<Integer>();
        directions.add(-5);
        DirectionCollection dc2 = new DirectionCollection(directions, 0);
        for (int i = x; i < finishX; i++) {
            World.World_Objects[getArea(i, y)].put(new BlockPoint(i, y, height, 0), dc2);
        }
        for (int i = y; i < finishY; i++) {
            World.World_Objects[getArea(x, i)].put(new BlockPoint(x, i, height, 0), dc2);
        }
        for (int i = x; i < finishX; i++) {
            World.World_Objects[getArea(i, finishY)].put(new BlockPoint(i, finishY, height, 0), dc2);
        }
        for (int i = y; i < finishY; i++) {
            World.World_Objects[getArea(finishX, i)].put(new BlockPoint(finishX, i, height, 0), dc2);
        }
        //}
    }

    public static int higherdir(int dir, int by) {
        int newdir = dir + by;
        if (newdir <= 3 && newdir >= 0) {
            return newdir;
        } else {
            if (newdir > 3) {
                int toadd = newdir - 4;
                newdir = toadd;
            } else if (newdir < 0) {
                /*int toadd = newdir+4;
                newdir = newdir;*/
            }
            return newdir;
        }
    }

    public static int getnextDir(int dir, int absX, int absY, int dx, int dy, int playerX, int playerY, int heightLevel) {

        if (dir <= -1) {
            return -5;
        }

        int d0 = 0;
        d0 >>= 1;
        int d2 = 2;
        d2 >>= 1;
        int d4 = 4;
        d4 >>= 1;
        int d6 = 6;
        d6 >>= 1;
        int d8 = 8;
        d8 >>= 1;
        int d10 = 10;
        d10 >>= 1;
        int d12 = 12;
        d12 >>= 1;
        int d14 = 14;
        d14 >>= 1;

        switch (dir) {
            case 0: // N
                if (!checkPos(heightLevel, absX, absY, absX + dx, absY + dy, 0)) {
                    dir = -1;
                    if (playerX < absX && checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[d12], absY + Constants.DIRECTION_DELTA_Y[d12], 0)) {
                        dir = 12;
                    } else if (playerX > absX && checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[d4], absY + Constants.DIRECTION_DELTA_Y[d4], 0)) {
                        dir = 4;
                    }/* else {
                            if(isWalkAble(heightLevel,absX,absY,absX+Constants.DIRECTION_DELTA_X[d12],absY+Constants.DIRECTION_DELTA_Y[d12],0)) {
								dir = 12;
							} else if(isWalkAble(heightLevel,absX,absY,absX+Constants.DIRECTION_DELTA_X[d4],absY+Constants.DIRECTION_DELTA_Y[d4],0)) {
								dir = 4;
							}
						}*/
                }
                break;
            case 8: // S
                if (!checkPos(heightLevel, absX, absY, absX + dx, absY + dy, 0)) {
                    dir = -1;
                    if (playerX < absX && checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[d12], absY + Constants.DIRECTION_DELTA_Y[d12], 0)) {
                        dir = 12;
                    } else if (playerX > absX && checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[d4], absY + Constants.DIRECTION_DELTA_Y[d4], 0)) {
                        dir = 4;
                    }/* else {
                            if(isWalkAble(heightLevel,absX,absY,absX+Constants.DIRECTION_DELTA_X[d12],absY+Constants.DIRECTION_DELTA_Y[d12],0)) {
								dir = 12;
							} else if(isWalkAble(heightLevel,absX,absY,absX+Constants.DIRECTION_DELTA_X[d4],absY+Constants.DIRECTION_DELTA_Y[d4],0)) {
								dir = 4;
							}
						}*/
                }
                break;
            case 12: // W
                if (!checkPos(heightLevel, absX, absY, absX + dx, absY + dy, 0)) {
                    dir = -1;
                    if (playerY < absY && checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[d8], absY + Constants.DIRECTION_DELTA_Y[d8], 0)) {
                        dir = 8;
                    } else if (playerY > absY && checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[d0], absY + Constants.DIRECTION_DELTA_Y[d0], 0)) {
                        dir = 0;
                    }/* else {
                            if(isWalkAble(heightLevel,absX,absY,absX+Constants.DIRECTION_DELTA_X[d8],absY+Constants.DIRECTION_DELTA_Y[d8],0)) {
								dir = 8;
							} else if(isWalkAble(heightLevel,absX,absY,absX+Constants.DIRECTION_DELTA_X[d0],absY+Constants.DIRECTION_DELTA_Y[d0],0)) {
								dir = 0;
							}
						}*/
                }
                break;
            case 4: // O
                if (!checkPos(heightLevel, absX, absY, absX + dx, absY + dy, 0)) {
                    dir = -1;
                    if (playerY < absY && checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[d8], absY + Constants.DIRECTION_DELTA_Y[d8], 0)) {
                        dir = 8;
                    } else if (playerY > absY && checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[d0], absY + Constants.DIRECTION_DELTA_Y[d0], 0)) {
                        dir = 0;
                    }/* else {
                            if(isWalkAble(heightLevel,absX,absY,absX+Constants.DIRECTION_DELTA_X[d8],absY+Constants.DIRECTION_DELTA_Y[d8],0)) {
								dir = 8;
							} else if(isWalkAble(heightLevel,absX,absY,absX+Constants.DIRECTION_DELTA_X[d0],absY+Constants.DIRECTION_DELTA_Y[d0],0)) {
								dir = 0;
							}
						}*/
                }
                break;
            case 2: // NO
                if (!checkPos(heightLevel, absX, absY, absX + 1, absY, 0) || !checkPos(heightLevel, absX, absY, absX, absY + 1, 0)) {
                    dir = -1;
                }
                if (dir == -1 || !checkPos(heightLevel, absX, absY, absX + dx, absY + dy, 0)) {
                    dir = -1;
                    if (checkPos(heightLevel, absX, absY, absX, absY + 1, 0)) {
                        dir = 0;
                    } else if (checkPos(heightLevel, absX, absY, absX + 1, absY, 0)) {
                        dir = 4;
                    }
                }
                break;
            case 14: // NW
                if (!checkPos(heightLevel, absX, absY, absX - 1, absY, 0) || !checkPos(heightLevel, absX, absY, absX, absY + 1, 0)) {
                    dir = -1;
                }
                if (dir == -1 || !checkPos(heightLevel, absX, absY, absX + dx, absY + dy, 0)) {
                    dir = -1;
                    if (checkPos(heightLevel, absX, absY, absX, absY + 1, 0)) {
                        dir = 0;
                    } else if (checkPos(heightLevel, absX, absY, absX - 1, absY, 0)) {
                        dir = 12;
                    }
                }
                break;
            case 10: // SW
                if (!checkPos(heightLevel, absX, absY, absX - 1, absY, 0) || !checkPos(heightLevel, absX, absY, absX, absY - 1, 0)) {
                    dir = -1;
                }
                if (dir == -1 || !checkPos(heightLevel, absX, absY, absX + dx, absY + dy, 0)) {
                    dir = -1;
                    if (checkPos(heightLevel, absX, absY, absX, absY - 1, 0)) {
                        dir = 8;
                    } else if (checkPos(heightLevel, absX, absY, absX - 1, absY, 0)) {
                        dir = 12;
                    }
                }
                break;
            case 6: // SO
                if (!checkPos(heightLevel, absX, absY, absX + 1, absY, 0) || !checkPos(heightLevel, absX, absY, absX, absY - 1, 0)) {
                    dir = -1;
                }
                if (dir == -1 || !checkPos(heightLevel, absX, absY, absX + dx, absY + dy, 0)) {
                    dir = -1;
                    if (checkPos(heightLevel, absX, absY, absX, absY - 1, 0)) {
                        dir = 8;
                    } else if (checkPos(heightLevel, absX, absY, absX + 1, absY, 0)) {
                        dir = 4;
                    }
                }
                break;
            default:
                return getnextDir(dir - 1, absX, absY, dx, dy, playerX, playerY, heightLevel);
        }
        if (dir == -1) {
            return -5;
        }
        dir >>= 1;
        dx = Constants.DIRECTION_DELTA_X[dir];
        dy = Constants.DIRECTION_DELTA_Y[dir];

        if (!checkPos(heightLevel, absX, absY, absX + Constants.DIRECTION_DELTA_X[dir], absY + Constants.DIRECTION_DELTA_Y[dir], 0)) {
            return -5;
        }

        return dir;
    }


    public static int[] shootable = new int[15000];

    public static void loadHeights() {
        boolean EndOfFile = false;
        BufferedReader characterfile = null;
        String line = "";
        try {
            characterfile = new BufferedReader(new FileReader("./data/objHeight.txt"));
        } catch (FileNotFoundException fileex) {
            fileex.printStackTrace();
            return;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return;
        }
        while ((EndOfFile == false) && (line != null)) {
            line = line.trim();
            int j1 = Integer.parseInt(line.split("	")[1]);
            if (j1 < 135) {
                shootable[Integer.parseInt(line.split("	")[0])] = 1;
            }
            try {
                line = characterfile.readLine();
            } catch (Exception e) {
            }
        }
        try {
            characterfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	/*public static int[] objX = new int[1280619];
	public static int[] objY = new int[1280619];
	public static int[] objZ = new int[1280619];
	
	public static boolean isClose(int a,int b){
		for(int i = 0; i < 1280618; i++) {
			if(Constants.distance(a,b,objX[i],objY[i]) <= 100)
				return true;
		}
		return false;
	}
	
	public static int[] goodX = new int[100000];
	public static int[] goodY = new int[100000];
	public static int index99 = -1;
	
	public static int staticX = 2;
	public static int staticY = 2400;
	public static int hash34 = 0;
	
	public static void dumpObjects(){//this is used to force load all regions,
	//well store the needed data client side then dump once its finished collecting all the data
		//104x104 - each region size
		//System.out.println("dumping objects: "+staticX);
		if(staticX >= 12000){
			staticY += 104;
			staticX = 2;
			System.out.println("Y value: "+staticY);
			hash34++;//92 of these
		} else {
			staticX += 104;
		}
		if(staticY >= 12000){
			return;
		}
		if(isClose(staticX,staticY)){
			index99++;
			goodX[index99] = staticX;
			goodY[index99] = staticY;
		}
		if(hash34 >= 5){
			hash34 = 0;
			EventManager.getSingleton().addEvent(null, new Event() {
				public void execute(EventContainer container) {
					dumpObjects();
					
				}
				public void stop() {
				}

			}, 500);
			return;
		} else {
		dumpObjects();
		}
	}*/


    private static void loadWorldMap2() {
        RandomAccessFile in = null;
        int counter = 0;
        int toadd = 70;
        byte[] cache = null;
        int ptr = 0;
        try {
            in = new RandomAccessFile("./data/worldmap.bin", "r");
            cache = new byte[(int) in.length()];
            in.read(cache, 0, (int) in.length());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //int[] altars = {2478,2479,2480,2481,2482,2483,2484,2485,2486,2487,2488,};
        //int j = 0;
        for (int i = 0; ptr < (cache.length - 1); i++/*,j++*/) {
            int id = 0, tileX = 0, tileY = 0, height = 0, type = 0, face = 0, face2 = 0;
            try {
                id = (((cache[ptr++] & 0xFF) << 8) | (cache[ptr++] & 0xFF));
                tileX = (((cache[ptr++] & 0xFF) << 8) | (cache[ptr++] & 0xFF));
                tileY = (((cache[ptr++] & 0xFF) << 8) | (cache[ptr++] & 0xFF));
                height = cache[ptr++] & 0xFF;
                type = cache[ptr++] & 0xFF;
                face2 = face = cache[ptr++] & 0xFF;
                if (id == 2560 || id == 2561 || id == 2562 || id == 2563 || id == 2564 || id == 2565) {
                    thiefstalls.put((tileX * 5000 + tileY), face2);
                }
                if (id == 2213 || id == 2214 || id == 3045 || id == 5276 || id == 6084 || id == 10517 || id == 11338 || id == 11758 ||
                        id == 12798 || id == 12799 || id == 12800 || id == 3193 || id == 12801 || id == 12120 || id == 12121)
                    bankBooths.put((tileX * 5000 + tileY), face2);
                ObjectManager.addMapObject(tileX, tileY, height, id);

				/*if(tileX >= 2814 && tileX <= 2942 && tileY >= 5250 && tileY <= 5373)
					height--;*/
                if (tileX == 2894 && tileY == 5304)
                    System.out.println("height: " + height + " type: " + type + " face: " + face + " booleans, " + hasActions[id] + "," + isClipped[id]);
				
				
				
				/*objX[i] = tileX;
				objY[i] = tileY;
				objZ[i] = height;*/
				
				/*if(id == 2213 || id == 2214 || id == 3045 || id == 5276 || id == 6084){
					System.out.println("bank: "+id+"	"+tileX+"	"+tileY);
				}*/
				/*if(id == 2489 || id == 2490){
					System.out.println("runeAltar id: "+id+" : "+tileX+" : "+tileY);
				}*/
				/*if(j == 0){
					LogDumper.dump2("./config/objectdump.sql","INSERT INTO `objectdump` (`id`,`x`,`y`,`z`,`t`,`f`) VALUES");
				}
				if(j == 99){
					LogDumper.dump2("./config/objectdump.sql","("+id+","+tileX+","+tileY+","+height+","+type+","+face+");");
					j = -1;
				} else {
					LogDumper.dump2("./config/objectdump.sql","("+id+","+tileX+","+tileY+","+height+","+type+","+face+"),");
				}*/
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            int s = 0;
            int part = 0;
            boolean nb = false;


            if ((type >= 12 && type < 22) || type == 3) {
                continue;
            }

            BlockPoint[] adder = new BlockPoint[toadd];
            int[] adderdir = new int[toadd];
            int sizeY = getSizeY(id);
            int sizeX = getSizeX(id);
            int dir2 = -1;
            for (int i2 = 0; i2 < toadd; i2++) {
                adderdir[i2] = -1;
            }

			/*if((tileX == 3023 && tileY == 3627) || (tileX == 3021 && tileY ==3631)) {
				System.out.println("object: "+id+", face: "+face+", type: "+type+", X "+tileX+", Y "+tileY);
			}*/
            if (type == 22) {

                if (!hasActions[id] && !isClipped[id]) {
                    type = 10;
                } else
                    continue;
            }
            if (type <= 5) {
                if (type == 0) {
                    if (face == 0) { // main 4 directions basically
                        face = 12;
                    } else if (face == 1) {
                        face = 0;
                    } else if (face == 2) {
                        face = 4;
                    } else if (face == 3) {
                        face = 8;
                    } else {
                        System.out.println("Invalid face: " + face);
                    }
                } else if (type == 1) { // diagonal faces
                    if (face == 0) {
                        face = -1;
                        tileX += 1;
                    } else if (face == 1) {
                        face = -1;
                        tileY += 1;
                    } else if (face == 2) {
                        face = -1;
                        tileX += 1;
                    } else if (face == 3) {
                        face = -1;
                        tileY += 1;
                    } else {
                        System.out.println("Invalid face: " + face);
                    }
                } else if (type == 2) { // strange face, seems to be all diagonal directions
                    if (face == 0) {
                        face = -1;
                    } else if (face == 1) {
                        face = -1;
                    } else if (face == 2) {
                        face = -1;
                    } else if (face == 3) {
                        face = -1;
                    } else {
                        System.out.println("Invalid face: " + face);
                    }
                } else if (type == 4) { // all directions
                    if (face == 0) {
                        face = 4;
                    } else if (face == 1) {
                        face = 0;
                    } else if (face == 2) {
                        face = 12;
                    } else if (face == 3) {
                        face = 8;
                    } else {
                        System.out.println("Invalid face: " + face);
                    }
                } else if (type == 5 || type == 3) { // some houses edges, outside -> walkable
                    face = -1;
                    nb = true;
                } else if (type >= 6 && type <= 9) { // diagonal walls, decorations, etc...
                    face = -1;
                }
            } else {
				/*if(id == 1722 || id == 11727 || id == 1752 || id == 1747 || id == 1750 || id == 1738 || id == 1757 || id == 1755 || id == 1725 || id == 11736 || id == 11732 || id == 9470 || id == 9558 || id == 11740 || id == 11739 || id == 1738 || id == 9582 || id == 9558) {
					Handlers.StairHandler.addStair(id,tileX,tileY,height,true,face,sizeX,sizeY);
				} else if(id == 11728 || id == 1733 || id == 1746 || id == 1749 || id == 1744 || id == 4911 || id == 1740 || id == 1723 || id == 1734 || id == 1726 || id == 11737 || id == 11733 || id == 9471 || id == 9559 || id == 11741 || id == 1740 || id == 9559 || id == 9584) {
					Handlers.StairHandler.addStair(id,tileX,tileY,height,false,face,sizeX,sizeY);
				}*/
                if ((face == 0 || face == 2) && type >= 10) {
                    int ys = sizeY;
                    sizeY = sizeX;
                    sizeX = ys;
                }
                face = -1;
            }
            int dir = face;
            if (id < 15000)
                s = shootable[id];
            if ((id == 9374) || (id == 4513) || (id >= 4518 && id <= 4520) || (id >= 5122 && id <= 5125) || (id == 5112) || (id >= 1140 && id <= 1205) || (id >= 4735 && id <= 4740) || (id <= 1299 && id >= 1298) || (id == 1174) || (id >= 446 && id <= 447) || (id >= 1240 && id <= 1265) || (id >= 950 && id <= 953) || (id >= 4342 && id <= 4345) || (id >= 3948 && id <= 3950) || id == 1032 || id == 4436 || id == 4446 || id == 4447) {
                nb = true;
                s = 1;
            } else if ((id == 5113) || (id >= 471 && id <= 474) || (id == 1161) || (id == 312) || (id == 1341) || (id >= 312 && id <= 313) || (id == 1341) || id == 336 || id == 11603 || (id >= 11930 && id <= 11945) || id == 11629 || id == 9623 || (id == 1392) || (id == 1394) || (id >= 980 && id <= 982) || (id == 1297) || (id == 9375)) {
                s = 1;
            } else if ((tileX <= 2425 && tileY <= 3091 && tileX >= 2411 && tileY >= 3085) || (tileX >= 2413 && tileY >= 3074 && tileX <= 2417 && tileY <= 3084) || (tileX >= 2374 && tileY >= 3116 && tileX <= 2387 && tileY <= 3122) || (tileX <= 2386 && tileY <= 3133 && tileX >= 2382 && tileY >= 3122)) {
                s = 1;
            } else if (id >= 6716 && id <= 6750) {
                dir = -1;
            }

            if (!nb) {
                if (sizeY >= 2 || sizeX >= 2) {
                    for (int i3 = 0; i3 < sizeY; i3++) {
                        for (int i2 = 0; i2 < sizeX; i2++) {
                            if (part > 65)
                                continue;
                            adder[part++] = new BlockPoint(tileX + i3, tileY + i2, height, s);

                        }
                    }
                } else {
                    if (dir != -1) {
                        if (dir == 8) { // S
                            adderdir[part] = 0;
                            adder[part++] = new BlockPoint(tileX, tileY - 1, height, s);
                            dir2 = 0;
                        } else if (dir == 4) { // O
                            adderdir[part] = 4;
                            adder[part++] = new BlockPoint(tileX + 1, tileY, height, s);
                            dir2 = 12;
                        } else if (dir == 0) { // N
                            adderdir[part] = 8;
                            adder[part++] = new BlockPoint(tileX, tileY + 1, height, s);
                            dir2 = 8;
                        } else if (dir == 12) { // W
                            adderdir[part] = 12;
                            adder[part++] = new BlockPoint(tileX - 1, tileY, height, s);
                            dir2 = 4;
                        } else if (dir == 2) { // NO
                        } else if (dir == 6) { // SO
                        } else if (dir == 14) { // NW
                        } else if (dir == 10) { // SW
                        }
                    }
                    adder[part++] = new BlockPoint(tileX, tileY, height, s);
                }
            }

            int which = getArea(tileX, tileY);
            if (which == 655655)
                continue;
            for (int i2 = 0; i2 < part; i2++) {
                if (dir2 != -1 && i2 == 1) {
                    dir = dir2;
                }
                if (World.World_Objects[which].containsKey(adder[i2])) {
                    DirectionCollection dircol = World.World_Objects[which].get(adder[i2]);
                    ArrayList<Integer> directions = new ArrayList<Integer>();
                    for (int i3 : dircol.directions) {
                        directions.add(i3);
                    }
                    if (!directions.contains(dir)) {
                        ArrayList<Integer> dirstoadd = new ArrayList<Integer>();
                        if (!directions.contains(-1)) {
                            for (int i3 : directions) {
                                if (!dirstoadd.contains(i3)) {
                                    dirstoadd.add(i3);
                                }
                            }
                            dirstoadd.add(dir);
                        } else {
                            dirstoadd.add(-1);
                        }
                        DirectionCollection dc2 = new DirectionCollection(dirstoadd, face2);
                        World.World_Objects[which].remove(adder[i2]);
                        World.World_Objects[which].put(adder[i2], dc2);
                        counter++;
                        ///*doors*/if(id == 1531|| id == 1534 || id == 1537 || id == 11708 || id == 11715 || id == 1590 || id == 1530 || id == 1533 || id == 1536  || id == 10529 || id == 14749 || id == 1533 || id == 1519 || id == 11707 || id == 11714 || id == 11721 || id == 9563 || id == 2647 || id == 2595 || id == 2399 || id == 2112 || id == 1591 || id == 59
                        ///*gates*/	|| id == 37 || id == 38 || id == 39 || id == 47 || id == 48 || id == 49 || id == 50 || id == 52 || id == 53 || id == 89 || id == 90 || id == 94 || id == 95 || id == 166 || id == 167 || id == 190 || id == 788 || id == 789 || id == 1551 || id == 1552 || id == 1553 || id == 1556 || id == 1557 || id == 1558 || id == 1559 || id == 1560 || id == 1561 || id == 1589 || id == 1590 || id == 1596 || id == 1597 || id == 1598 || id == 1599 || id == 2039 || id == 2041 || id == 2050 || id == 2051 || id == 2058 || id == 2060 || id == 2115 || id == 2116 || id == 2154 || id == 2155 || id == 2199 || id == 2200 || id == 2255 || id == 2256 || id == 2259 || id == 2260 || id == 2261 || id == 2262 || id == 2307 || id == 2308 || id == 2391 || id == 2392 || id == 2394 || id == 2432 || id == 2433 || id == 2438 || id == 2439 || id == 2552 || id == 2553 || id == 2607 || id == 2608 || id == 2623 || id == 2664 || id == 2665 || id == 2673 || id == 2674 || id == 2685 || id == 2686 || id == 2687 || id == 2688 || id == 2786 || id == 2787 || id == 2788 || id == 2789 || id == 2814 || id == 2815 || id == 2865 || id == 2866 || id == 2882 || id == 2883 || id == 2912 || id == 2913 || id == 2922 || id == 2923 || id == 2924 || id == 2925 || id == 2930 || id == 2931 || id == 3015 || id == 3016 || id == 3020 || id == 3021 || id == 3022 || id == 3023 || id == 3197 || id == 3198 || id == 3444 || id == 3445 || id == 3506 || id == 3507 || id == 3725 || id == 3726 || id == 3727 || id == 3728 || id == 3944 || id == 3945 || id == 3946 || id == 3947 || id == 4139 || id == 4140 || id == 4311 || id == 4312 || id == 4313 || id == 4787 || id == 4788 || id == 5043 || id == 5044 || id == 6451 || id == 6452 || id == 6461 || id == 6462 || id == 6566 || id == 6615 || id == 7049 || id == 7050 || id == 7051 || id == 7052 || id == 8810 || id == 8811 || id == 8812 || id == 8813 || id == 9140 || id == 9141 || id == 9142) {
                        //		world.doors.addDoor(dc2,id,tileX,tileY,height,face2);
                    }

                } else {
                    ArrayList<Integer> dirstoadd = new ArrayList<Integer>();
                    dirstoadd.add(dir);
                    DirectionCollection dc = new DirectionCollection(dirstoadd, face2);
                    World.World_Objects[which].put(adder[i2], dc);
                    counter++;
                    //		/*doors*/if(id == 1531|| id == 1534 || id == 1537 || id == 11708 || id == 11715 || id == 1590 || id == 1530 || id == 1533 || id == 1536  || id == 10529 || id == 14749 || id == 1533 || id == 1519 || id == 11707 || id == 11714 || id == 11721 || id == 9563 || id == 2647 || id == 2595 || id == 2399 || id == 2112 || id == 1591 || id == 59
                    //		/*gates*/	|| id == 37 || id == 38 || id == 39 || id == 47 || id == 48 || id == 49 || id == 50 || id == 52 || id == 53 || id == 89 || id == 90 || id == 94 || id == 95 || id == 166 || id == 167 || id == 190 || id == 788 || id == 789 || id == 1551 || id == 1552 || id == 1553 || id == 1556 || id == 1557 || id == 1558 || id == 1559 || id == 1560 || id == 1561 || id == 1589 || id == 1590 || id == 1596 || id == 1597 || id == 1598 || id == 1599 || id == 2039 || id == 2041 || id == 2050 || id == 2051 || id == 2058 || id == 2060 || id == 2115 || id == 2116 || id == 2154 || id == 2155 || id == 2199 || id == 2200 || id == 2255 || id == 2256 || id == 2259 || id == 2260 || id == 2261 || id == 2262 || id == 2307 || id == 2308 || id == 2391 || id == 2392 || id == 2394 || id == 2432 || id == 2433 || id == 2438 || id == 2439 || id == 2552 || id == 2553 || id == 2607 || id == 2608 || id == 2623 || id == 2664 || id == 2665 || id == 2673 || id == 2674 || id == 2685 || id == 2686 || id == 2687 || id == 2688 || id == 2786 || id == 2787 || id == 2788 || id == 2789 || id == 2814 || id == 2815 || id == 2865 || id == 2866 || id == 2882 || id == 2883 || id == 2912 || id == 2913 || id == 2922 || id == 2923 || id == 2924 || id == 2925 || id == 2930 || id == 2931 || id == 3015 || id == 3016 || id == 3020 || id == 3021 || id == 3022 || id == 3023 || id == 3197 || id == 3198 || id == 3444 || id == 3445 || id == 3506 || id == 3507 || id == 3725 || id == 3726 || id == 3727 || id == 3728 || id == 3944 || id == 3945 || id == 3946 || id == 3947 || id == 4139 || id == 4140 || id == 4311 || id == 4312 || id == 4313 || id == 4787 || id == 4788 || id == 5043 || id == 5044 || id == 6451 || id == 6452 || id == 6461 || id == 6462 || id == 6566 || id == 6615 || id == 7049 || id == 7050 || id == 7051 || id == 7052 || id == 8810 || id == 8811 || id == 8812 || id == 8813 || id == 9140 || id == 9141 || id == 9142) {
                    //				world.doors.addDoor(dc,id,tileX,tileY,height,face2);
                    //			}
                }
            }
			/*if(dc != null){
				if(id == 1531|| id == 1534 || id == 1537 || id == 11708 || id == 11715 || id == 1590 || id == 1530 || id == 1533 || id == 1536  || id == 10529 || id == 14749 || id == 1533 || id == 1519 || id == 11707 || id == 11714 || id == 11721 || id == 9563 || id == 2647 || id == 2595 || id == 2399 || id == 2112 || id == 1591 || id == 59) {
					world.doors.addDoor(dc,id,tileX,tileY,height,face);
				}
			} else if(dc2 != null){
				if(id == 1531|| id == 1534 || id == 1537 || id == 11708 || id == 11715 || id == 1590 || id == 1530 || id == 1533 || id == 1536  || id == 10529 || id == 14749 || id == 1533 || id == 1519 || id == 11707 || id == 11714 || id == 11721 || id == 9563 || id == 2647 || id == 2595 || id == 2399 || id == 2112 || id == 1591 || id == 59) {
					world.doors.addDoor(dc2,id,tileX,tileY,height,face);
				}
			}*/

            adder = null;
            adderdir = null;

        }
        if(Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
            Server.getLogger().log(Level.INFO, "Successfully loaded " + counter + " World Objects.");
        //ObjectClickHandler.loaded = true;

        //new Thread(() -> world.getObjectMap().toMap()).start();
        //System.out.println("Highest area: "+highest);
        cache = null;
        in = null;
    }


    public static int getArea(int x, int y) {
        String a = (y / 100) + "" + (x / 100);
        int area = Integer.parseInt(a);
        if (area > highest) {
            //System.out.println("out of range: "+x+" "+y+" "+a);
            highest = area;
        }
        return area;
    }

    public static void loadObjectSizes() {
        RandomAccessFile in = null;
        byte[] cache = null;
        int ptr = 0;
        try {
            in = new RandomAccessFile("./data/worldmapdef.bin", "r");
            cache = new byte[(int) in.length()];
            in.read(cache, 0, (int) in.length());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sizeX = new byte[38000];
        sizeY = new byte[38000];
        hasActions = new boolean[38000];
        isClipped = new boolean[38000];

        for (int i = 0; i < 38000; i++) {
            @SuppressWarnings("unused")
            int id = (((cache[ptr++] & 0xFF) << 8) | (cache[ptr++] & 0xFF));
            while ((cache[ptr++] & 0xFF) != 10) {
            }
            sizeX[i] = (byte) (cache[ptr++] & 0xFF);
            sizeY[i] = (byte) (cache[ptr++] & 0xFF);
            if (sizeX[i] > 65)
                sizeX[i] = 65;
            if (sizeY[i] > 65)
                sizeY[i] = 65;
            hasActions[i] = ((cache[ptr++] & 0xFF) == 1);
            isClipped[i] = ((cache[ptr++] & 0xFF) == 1);
        }
    }

    public static void loadObjectSizes2() {
        sizeX = new byte[14210];
        sizeY = new byte[14210];
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader("./data/objectSize.cfg"));
        } catch (FileNotFoundException fileex) {
            fileex.printStackTrace();
            return;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return;
        }
        while ((EndOfFile == false) && (line != null)) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.startsWith("object")) {
                    int id = Integer.parseInt(token3[0]);
                    String size = token3[2];
                    String[] sizes = size.split("x");
                    sizeX[id] = (byte) Integer.parseInt(sizes[0]);
                    sizeY[id] = (byte) Integer.parseInt(sizes[1]);
                }
            } else {
                if (line.equals("[END]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {
                    }
                    return;
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {
        }
    }


    public static boolean projectileClear(Position from, Position to) {
        int currentX = from.getX(), currentY = from.getY();
        int newX = from.getX(), newY = from.getY();
        while (currentX != to.getX() || currentY != to.getY()) {
            if (currentX < to.getX())
                newX = currentX + 1;
            else if (currentX > to.getX())
                newX = currentX - 1;
            if (currentY < to.getY())
                newY = currentY + 1;
            else if (currentY > to.getY())
                newY = currentY - 1;
            if (!checkPos(from.getZ(), currentX, currentY, newX, newY, 1))
                return false;
            currentX = newX;
            currentY = newY;
        }
        return true;
    }

    public static boolean projectileClear(int height, int absX, int absY, int toX, int toY) {
        int currentX = absX, currentY = absY;
        int newX = absX, newY = absY;
        while (currentX != toX || currentY != toY) {
            if (currentX < toX)
                newX = currentX + 1;
            else if (currentX > toX)
                newX = currentX - 1;
            if (currentY < toY)
                newY = currentY + 1;
            else if (currentY > toY)
                newY = currentY - 1;
            if (!checkPos(height, currentX, currentY, newX, newY, 1))
                return false;
            currentX = newX;
            currentY = newY;
        }
        return true;
    }
	/*public static boolean projectileClear(int height,int absX,int absY,int toX,int toY){
		int xDis = distance(absX,0,toX,0);
		int yDis = distance(0,absY,0,toY);
		int lastX = absX;
		int lastY = absY;
		boolean xLarge = Math.max(xDis, yDis) == xDis;
		int stepSize = 0;
		if(xDis == 0 || yDis == 0)
			stepSize = -1;
		else if(xDis >= yDis)
			stepSize = (xDis/yDis);
		else
			stepSize = (yDis/xDis);
		for(int i = 1, j = 0, g = 0; i < Math.max(xDis, yDis); i++,j++){
			if(j == stepSize){
				g++;
				j = 0;
			}
			if(xLarge){
				if(!isWalkAble(height, lastX, lastY, lastX+i, lastY+g, 1))
					return false;
				lastX = absX+i;
				lastY = absY+g;
			} else {
				if(!isWalkAble(height, lastX, lastY, lastX+g, lastY+i, 1))
					return false;
				lastY = absY+i;
				lastX = absX+g;
			}
		}
		return true;
	}*/

    public static boolean checkPos(int height, int absX, int absY, int toAbsX, int toAbsY, int check) {
        height = height % 4;
        if (absX == toAbsX || absY == toAbsY) {
            return isWalkAble2(height, absX, absY, toAbsX, toAbsY, check);
        } else {
            if (!isWalkAble2(height, absX, absY, toAbsX, absY, check))
                return false;
            else if (!isWalkAble2(height, absX, absY, absX, toAbsY, check))
                return false;
            else if (!isWalkAble2(height, toAbsX, absY, toAbsX, toAbsY, check))
                return false;
            else if (!isWalkAble2(height, absX, toAbsY, toAbsX, toAbsY, check))
                return false;
        }
        return true;
        //return isWalkAble2(height,absX,absY,toAbsX,toAbsY,check);
    }


    /*public static boolean isWalkAble(int height, int absX, int absY, int toAbsX, int toAbsY, int check) {
        if (absX == toAbsX && absY == toAbsY) { return true; }
        BlockPoint f = new BlockPoint(toAbsX,toAbsY,height,0);
        BlockPoint f2 = new BlockPoint(toAbsX,toAbsY,height,1);
        int which = getArea(toAbsX,toAbsY);

        int dir = Main.Constants.direction(absX,absY,toAbsX,toAbsY);
        int objdir = -1;
        int objdir2 = -1;

        if(world.World_Objects[which].containsKey(f)) {
            objdir = world.World_Objects[which].get(f);
        } else if(world.World_Objects[which].containsKey(f2)) {
            objdir2 = world.World_Objects[which].get(f2);
        }

        if(objdir == -1 && objdir2 == -1) {
            dir = -1;
        }

        if(absX > 0 && dir != -1) {
            if(check == 0) {
                if(dir == 2) {
                    if(isWalkAble(height,absX,absY,toAbsX-1,toAbsY,check) && isWalkAble(height,absX,absY,toAbsX,toAbsY-1,check)) {
                        return true;
                    }
                    return false;
                } else if(dir == 14) {
                    if(isWalkAble(height,absX,absY,toAbsX+1,toAbsY,check) && isWalkAble(height,absX,absY,toAbsX,toAbsY-1,check)) {
                            return true;
                    }
                    return false;
                } else if(dir == 10) {
                    if(isWalkAble(height,absX,absY,toAbsX+1,toAbsY,check) && isWalkAble(height,absX,absY,toAbsX,toAbsY+1,check)) {
                        return true;
                    }
                    return false;
                } else if(dir == 6) {
                    if(isWalkAble(height,absX,absY,toAbsX-1,toAbsY,check) && isWalkAble(height,absX,absY,toAbsX,toAbsY+1,check)) {
                        return true;
                    }
                    return false;
                }
            }

            if(check == 1 && world.World_Objects[which].containsKey(f2) && objdir2 != dir) {
                return true;
            } else if(check == 0 && world.World_Objects[which].containsKey(f2) && objdir2 == dir) {
                return false;
            } else if(world.World_Objects[which].containsKey(f) && objdir == dir) {
                return false;
            }
        } else {
            if(check == 1 && world.World_Objects[which].containsKey(f2)) {
                return true;
            } else if(check == 0 && world.World_Objects[which].containsKey(f2)) {
                return false;
            } else if(world.World_Objects[which].containsKey(f)) {
                return false;
            }
        }
        return true;

    }*/

    public static int getFace(int toAbsX, int toAbsY, int height) {
        BlockPoint f = new BlockPoint(toAbsX, toAbsY, height, 0);
        int which = getArea(toAbsX, toAbsY);
        if (World.World_Objects[which] == null) {
            return -1;
        }

        if (World.World_Objects[which].containsKey(f)) {
            DirectionCollection dc = World.World_Objects[which].get(f);
            return dc.face;
        }
        return -1;
    }

    private static final List<Integer> walkableObjects = Arrays.asList(6591, 2475, 2476, 2477);
    private static boolean isWalkableObject(final int value) {
        return walkableObjects.stream().anyMatch(array -> array.equals(value));
    }

    public static boolean isWalkAble2(int height, int absX, int absY, int toAbsX, int toAbsY, int check) {
        //System.out.println(String.format("%d %d %d %d %d %d", height, absX, absY, toAbsX, toAbsY, check));
        //if(world == null) return true;
        if (absX == toAbsX && absY == toAbsY) {
            return true;
        }
		/*if(absX != toAbsX && absY != toAbsY){
			if(isWalkAble(height,absX,absY,toAbsX,absY,check) &&
				isWalkAble(height,absX,absY,absX,toAbsY,check) &&
				isWalkAble(height,toAbsX,absY,toAbsX,toAbsY,check) &&
				isWalkAble(height,absX,toAbsY,toAbsX,toAbsY,check))
					return true;
		}*/
        BlockPoint f = new BlockPoint(toAbsX, toAbsY, height, 0);
        BlockPoint f2 = new BlockPoint(toAbsX, toAbsY, height, 1);
        int which = getArea(toAbsX, toAbsY);

        //int dir = DirectionUtils.direction(toAbsX-absX,toAbsY-absY);
        int dir = direction(absX, absY, toAbsX, toAbsY);
        DirectionCollection dc = null;
        DirectionCollection dc2 = null;
        if (which > World.World_Objects.length) {
            System.out.println("error in WorldMap X: " + absX + " Y: " + absY);
            return false;
        }
        final GameObject obj = ObjectManager.getObjectAt(absX, absY, 0);
        if (obj != null) {
            return isWalkableObject(obj.getDefinition().getId());
        }
        if (World.World_Objects[which] != null && which < World.World_Objects[which].size()) {
            return true;
        }
        if (World.World_Objects[which] == null) {
            return true;
        }
        if (World.World_Objects[which].containsKey(f)) {
            dc = World.World_Objects[which].get(f);
        } else if (World.World_Objects[which].containsKey(f2)) {
            dc2 = World.World_Objects[which].get(f2);
        }

        if (dc == null && dc2 == null) {
            return true;
        }

        ArrayList<Integer> dirs = null;

        if (dc != null) {
            dirs = dc.directions;
        } else if (dc2 != null) {
            dirs = dc2.directions;
        }
        if (dirs.get(0) == -5) {
            //System.out.println("godwars wall active.");
            return false;
        }


        for (int i : dirs) {
            //System.out.print("   "+i);
            if (dir == 2) {
                return isWalkAble2(height, absX, absY, toAbsX - 1, toAbsY, check) && isWalkAble2(height, absX, absY, toAbsX, toAbsY - 1, check);
            } else if (dir == 14) {
                return isWalkAble2(height, absX, absY, toAbsX + 1, toAbsY, check) && isWalkAble2(height, absX, absY, toAbsX, toAbsY - 1, check);
            } else if (dir == 10) {
                return isWalkAble2(height, absX, absY, toAbsX + 1, toAbsY, check) && isWalkAble2(height, absX, absY, toAbsX, toAbsY + 1, check);
            } else if (dir == 6) {
                return isWalkAble2(height, absX, absY, toAbsX - 1, toAbsY, check) && isWalkAble2(height, absX, absY, toAbsX, toAbsY + 1, check);
            } else if (i != -1) {
				/*if(check == 1 && world.World_Objects[which].containsKey(f2) && i == dir) {
					return false;
				} else if(check == 0 && world.World_Objects[which].containsKey(f2) && i == dir) {
					return false;
				} else if(world.World_Objects[which].containsKey(f) && i == dir) {
					return false;
				}*/
                if (check == 1 && World.World_Objects[which].containsKey(f2)) {
                    return true;
                } else if (check == 0 && World.World_Objects[which].containsKey(f2) && i == dir) {
                    return false;
                } else if (World.World_Objects[which].containsKey(f) && i == dir) {
                    return false;
                }
            } else {
                if (check == 1 && World.World_Objects[which].containsKey(f2)) {
                    return true;
                } else if (check == 0 && World.World_Objects[which].containsKey(f2)) {
                    return false;
                } else if (World.World_Objects[which].containsKey(f)) {
                    return false;
                }
            }
        }
        return true;

    }

    public static int direction(int srcX, int srcY, int destX, int destY) {
        int dx = destX - srcX, dy = destY - srcY;
        // a lot of cases that have to be considered here ... is there a more
        // sophisticated (and quick!) way?
        if (dx < 0) {
            if (dy < 0) {
                if (dx < dy)
                    return 11;
                else if (dx > dy)
                    return 9;
                else
                    return 10; // dx == dy
            } else if (dy > 0) {
                if (-dx < dy)
                    return 15;
                else if (-dx > dy)
                    return 13;
                else
                    return 14; // -dx == dy
            } else { // dy == 0
                return 12;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                if (dx < -dy)
                    return 7;
                else if (dx > -dy)
                    return 5;
                else
                    return 6; // dx == -dy
            } else if (dy > 0) {
                if (dx < dy)
                    return 1;
                else if (dx > dy)
                    return 3;
                else
                    return 2; // dx == dy
            } else { // dy == 0
                return 4;
            }
        } else { // dx == 0
            if (dy < 0) {
                return 8;
            } else if (dy > 0) {
                return 0;
            } else { // dy == 0
                return -1; // src and dest are the same
            }
        }
    }


}