package glorydark.floatingtext.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import glorydark.floatingtext.FloatingTextMain;
import glorydark.floatingtext.entity.TextEntityData;
import glorydark.floatingtext.forms.FormFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class FloatingTextCommand extends Command {
    public FloatingTextCommand(String command) {
        super(command);

        this.getCommandParameters().clear();

        this.getCommandParameters().put("admin", new CommandParameter[]{
                CommandParameter.newEnum("admin", false, new String[]{"admin"})
        });

        this.getCommandParameters().put("reload", new CommandParameter[]{
                CommandParameter.newEnum("reload", false, new String[]{"reload"})
        });

        this.getCommandParameters().put("add", new CommandParameter[]{
                CommandParameter.newEnum("add", false, new String[]{"add"}),
                CommandParameter.newType("flag", false, CommandParamType.STRING),
                CommandParameter.newType("text", false, CommandParamType.STRING),
                CommandParameter.newEnum("tipsVariable", true, new CommandEnum("TipsVariable", "true", "false"))
        });
    }

    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.isPlayer() || commandSender.isOp()) {
            if (strings.length == 0) {
                return false;
            }
            String subCmdName = strings[0];
            switch (subCmdName) {
                case "reload":
                    FloatingTextMain.getInstance().loadAll();
                    commandSender.sendMessage("§aReload successfully!");
                    break;
                case "add": // ctc add name text enabletips
                    if (commandSender.isPlayer() && strings.length >= 3 && commandSender.isPlayer()) {
                        String name = strings[1];
                        String text = strings[2];
                        boolean enableTipsVariable = true;
                        if (strings.length == 4) {
                            enableTipsVariable = Boolean.parseBoolean(strings[3]);
                        }
                        Player player = (Player) commandSender;
                        TextEntityData data = new TextEntityData(name, player, new ArrayList<>(Arrays.asList(text.split("\\n"))), enableTipsVariable);
                        FloatingTextMain.getInstance().addFloatingText(data);
                        commandSender.sendMessage("§a成功添加浮空字至 " + player.getX() + "," + player.getY() + "," + player.getZ() + "," + player.getLevel().getName());
                        data.checkEntity();
//                        for (Player value : Server.getInstance().getOnlinePlayers().values()) {
//                            if (enableTipsVariable) {
//                                data.spawnTipsVariableFloatingTextTo(value);
//                            }
//                        }
                        return true;
                    }
                    break;
                case "admin":
                    if (commandSender.isPlayer() && commandSender.isOp() && Server.getInstance().getPluginManager().getPlugin("MemoriesOfTime-GameCore") != null) {
                        FormFactory.showAdminMain((Player) commandSender);
                    } else {
                        commandSender.sendMessage(TextFormat.RED + "Can not find the soft-depend: MemoriesOfTime-GameCore");
                    }
                    break;
            }
        }
        return false;
    }
}
