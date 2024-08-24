package glorydark.floatingtext.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Location;
import glorydark.floatingtext.FloatingTextMain;

import java.util.List;
import java.util.Map;

public class TextEntityData {

    protected String name;
    protected Location location;
    protected List<String> lines;
    protected String replacedText;
    protected boolean enableTipsVariable;

    private TextEntity textEntity;

    public TextEntityData(String name, Location location, List<String> lines, boolean enableTipsVariable) {
        this.name = name;
        this.location = location;
        this.enableTipsVariable = enableTipsVariable;
        setLines(lines);
    }

    public Location getLocation() {
        return this.location;
    }

    public List<String> getLines() {
        return this.lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
        StringBuilder text = new StringBuilder();
        if (!lines.isEmpty()) {
            for (int i = 0; i < lines.size(); i++) {
                text.append(lines.get(i));
                if (i != lines.size() - 1) {
                    text.append("\n");
                }
            }
        }
        this.replacedText = text.toString();
    }

    public String getText() {
        return this.replacedText;
    }

    @Deprecated
    public void spawnTipsVariableFloatingTextTo(Player player) {
        if (enableTipsVariable) {
            TextEntityWithTipsVariable entity = new TextEntityWithTipsVariable(this.location.getChunk(), Entity.getDefaultNBT(this.location), player, this);
            entity.spawnTo(player);
            entity.scheduleUpdate();
            this.textEntity = entity;
        }
    }

    @Deprecated
    public void spawnSimpleFloatingText() {
        TextEntity entity = new TextEntity(this.location.getChunk(), Entity.getDefaultNBT(this.location), null, this);
        entity.spawnToAll();
        entity.scheduleUpdate();
        if (FloatingTextMain.serverPlat.equals("mot")) {
            entity.setCanBeSavedWithChunk(false);
        }
        this.textEntity = entity;
    }

    public boolean isEnableTipsVariable() {
        return enableTipsVariable;
    }

    public String getName() {
        return name;
    }

    public void checkEntity() {
        if ((this.location.getLevel() == null && !Server.getInstance().loadLevel(this.location.getLevelName())) || this.location.getLevel().getProvider() == null) {
            FloatingTextMain.getInstance().getLogger().error("世界: " + this.location.getLevelName() + " 无法加载！NPC: " + this.getName() + "无法生成！");
            return;
        }

        if (this.textEntity != null && !this.textEntity.isClosed()) {
            return;
        }

        Map<Long, Player> players = this.location.getLevel().getPlayers();
        if (players.isEmpty()) {
            return;
        }

        if (!this.location.getLevel().isChunkLoaded(this.location.getChunkX(), this.location.getChunkZ())) {
            return;
        }

        if (enableTipsVariable) {
            TextEntityWithTipsVariable entity = new TextEntityWithTipsVariable(this.location.getChunk(), Entity.getDefaultNBT(this.location), null, this);
            entity.scheduleUpdate();
            if (FloatingTextMain.serverPlat.equals("mot")) {
                entity.setCanBeSavedWithChunk(false);
            }
            players.values().forEach(entity::spawnTo);
            this.textEntity = entity;
        } else {
            TextEntity entity = new TextEntity(this.location.getChunk(), Entity.getDefaultNBT(this.location), null, this);
            entity.scheduleUpdate();
            if (FloatingTextMain.serverPlat.equals("mot")) {
                entity.setCanBeSavedWithChunk(false);
            }
            entity.spawnToAll();
            this.textEntity = entity;
        }
    }
}
