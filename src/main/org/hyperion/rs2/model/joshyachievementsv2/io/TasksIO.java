package org.hyperion.rs2.model.joshyachievementsv2.io;

import java.util.List;
import java.util.Optional;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraints;
import org.hyperion.rs2.model.joshyachievementsv2.task.Task;
import org.hyperion.rs2.model.joshyachievementsv2.task.Tasks;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.BarrowsTripTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.BountyHunterKillTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.DungeoneeringFloorsTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.FightPitsTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.ItemOpenTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.KillForBountyTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.KillstreakTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.NpcKillTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.PickupItemTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.PlaceBountyTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.PlayerKillTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.SkillItemTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.SlayerTask;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.VoteTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TasksIO extends IOManager<Task, Tasks, TasksIO.TaskIO>{

    public interface TaskIO<T extends Task> extends IO<T>{

        default void encodeTask(final Document doc, final Element root, final T t){}

        T decodeTask(final Element root, final int id, final int threshold);

        default String tag(){
            return "task";
        }

        default void encode(final Document doc, final Element root, final T t){
            attr(root, "id", t.id);
            attr(root, "threshold", t.threshold);
            attr(root, "preTaskId", t.preTaskId);
            attr(root, "desc", t.desc);
            encodeTask(doc, root, t);
            root.appendChild(IO.constraints.out(doc, t.constraints));
        }

        default T decode(final Element root){
            final int id = intAttr(root, "id");
            final int threshold = intAttr(root, "threshold");
            final int preTaskId = intAttr(root, "preTaskId");
            final T task = decodeTask(root, id, threshold);
            if(preTaskId != 0)
                task.preTaskId = preTaskId;
            final Constraints constraints = IO.constraints.in(root);
            task.constraints.list.addAll(constraints.list);
            return task;
        }

    }

    protected TasksIO(){
        super("tasks", Tasks::new, Tasks::values);
    }

    protected void populate(){
        put(BarrowsTripTask.class, (TaskIO<BarrowsTripTask>)(r, id, threshold) -> new BarrowsTripTask(id, threshold));

        put(BountyHunterKillTask.class, (TaskIO<BountyHunterKillTask>)(r, id, threshold) -> new BountyHunterKillTask(id, threshold));

        put(FightPitsTask.class, new TaskIO<FightPitsTask>(){
            public void encodeTask(final Document doc, final Element root, final FightPitsTask t){
                attr(root, "result", t.result.name());
            }

            public FightPitsTask decodeTask(final Element root, final int id, final int threshold){
                final FightPitsTask.Result result = FightPitsTask.Result.valueOf(attr(root, "result"));
                return new FightPitsTask(id, result, threshold);
            }
        });

        put(ItemOpenTask.class, new TaskIO<ItemOpenTask>(){
            public void encodeTask(final Document doc, final Element root, final ItemOpenTask t){
                root.appendChild(ints(doc, "items", "item", "id", t.itemIds));
            }

            public ItemOpenTask decodeTask(final Element root, final int id, final int threshold){
                final List<Integer> itemIds = ints(root, "items", "item", "id");
                return new ItemOpenTask(id, itemIds, threshold);
            }
        });

        put(KillForBountyTask.class, new TaskIO<KillForBountyTask>(){
            public void encodeTask(final Document doc, final Element root, final KillForBountyTask t){
                attr(root, "accumulative", t.accumulative);
            }

            public KillForBountyTask decodeTask(final Element root, final int id, final int threshold){
                final boolean accumulative = boolAttr(root, "accumulative");
                return new KillForBountyTask(id, threshold, accumulative);
            }
        });

        put(KillstreakTask.class, (TaskIO<KillstreakTask>)(r, id, threshold) -> new KillstreakTask(id, threshold));

        put(NpcKillTask.class, new TaskIO<NpcKillTask>(){
            public void encodeTask(final Document doc, final Element root, final NpcKillTask t){
                root.appendChild(ints(doc, "npcs", "npc", "id", t.npcIds));
            }

            public NpcKillTask decodeTask(final Element root, final int id, final int threshold){
                final List<Integer> npcIds = ints(root, "npcs", "npc", "id");
                return new NpcKillTask(id, npcIds, threshold);
            }
        });

        put(PickupItemTask.class, new TaskIO<PickupItemTask>(){
            public void encodeTask(final Document doc, final Element root, final PickupItemTask t){
                attr(root, "from", t.from.name());
                root.appendChild(ints(doc, "items", "item", "id", t.itemIds));
            }

            public PickupItemTask decodeTask(final Element root, final int id, final int threshold){
                final PickupItemTask.From from = PickupItemTask.From.valueOf(attr(root, "from"));
                final List<Integer> itemIds = ints(root, "items", "item", "id");
                return new PickupItemTask(id, from, itemIds, threshold);
            }
        });

        put(PlaceBountyTask.class, new TaskIO<PlaceBountyTask>(){
            public void encodeTask(final Document doc, final Element root, final PlaceBountyTask t){
                attr(root, "accumulative", t.accumulative);
            }

            public PlaceBountyTask decodeTask(final Element root, final int id, final int threshold){
                final boolean accumulative = boolAttr(root, "accumulative");
                return new PlaceBountyTask(id, threshold, accumulative);
            }
        });

        put(PlayerKillTask.class, (TaskIO<PlayerKillTask>)(r, id, threshold) -> new PlayerKillTask(id, threshold));

        put(SkillItemTask.class, new TaskIO<SkillItemTask>(){
            public void encodeTask(final Document doc, final Element root, final SkillItemTask t){
                attr(root, "skill", t.skill);
                root.appendChild(ints(doc, "items", "item", "id", t.itemIds));
            }

            public SkillItemTask decodeTask(final Element root, final int id, final int threshold){
                final int skill = intAttr(root, "skill");
                final List<Integer> itemIds = ints(root, "items", "item", "id");
                return new SkillItemTask(id, skill, itemIds, threshold);
            }
        });

        put(VoteTask.class, (TaskIO<VoteTask>)(r, id, threshold) -> new VoteTask(id, threshold));

        put(SlayerTask.class, new TaskIO<SlayerTask>(){
            public void encodeTask(final Document doc, final Element root, final SlayerTask t){
                root.appendChild(ints(doc, "npcs", "npc", "id", t.npcIds));
            }

            public SlayerTask decodeTask(final Element root, final int id, final int threshold){
                final List<Integer> npcIds = ints(root, "npcs", "npc", "id");
                return new SlayerTask(id, npcIds, threshold);
            }
        });

        put(DungeoneeringFloorsTask.class, new TaskIO<DungeoneeringFloorsTask>(){
            public void encodeTask(final Document doc, final Element root, final DungeoneeringFloorsTask t){
                if(t.difficulty != null)
                    attr(root, "difficulty", t.difficulty.name());
                if(t.size != null)
                    attr(root, "size", t.size.name());
            }

            public DungeoneeringFloorsTask decodeTask(final Element root, final int id, final int threshold){
                final DungeoneeringFloorsTask.Difficulty difficulty = Optional.ofNullable(attr(root, "difficulty"))
                        .map(DungeoneeringFloorsTask.Difficulty::valueOf)
                        .orElse(null);
                final DungeoneeringFloorsTask.Size size = Optional.ofNullable(attr(root, "size"))
                        .map(DungeoneeringFloorsTask.Size::valueOf)
                        .orElse(null);
                return new DungeoneeringFloorsTask(id, difficulty, size, threshold);
            }
        });
    }
}
