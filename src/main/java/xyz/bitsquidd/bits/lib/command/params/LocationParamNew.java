package xyz.bitsquidd.bits.lib.command.params;

import org.bukkit.Location;
import org.bukkit.World;
import xyz.bitsquidd.bits.core.LogController;
import xyz.bitsquidd.bits.lib.command.CommandContextNew;
import xyz.bitsquidd.bits.lib.command.exceptions.ParamParseException;

import java.util.List;

public class LocationParamNew implements CommandParamNew<Location> {
    public static final LocationParamNew INSTANCE = new LocationParamNew();

    @Override
    public String getTypeName() {
        return "Location";
    }

    @Override
    public int getRequiredArgs() {
        return 3;
    }

    @Override
    public Location parse(CommandContextNew context, int startIndex) throws ParamParseException {
        double x = Double.parseDouble(context.args[startIndex]);
        double y = Double.parseDouble(context.args[startIndex+1]);
        double z = Double.parseDouble(context.args[startIndex+2]);
        World world = context.getWorld();

        return new Location(world, x,y,z);
    }

    @Override
    public boolean canParseArg(CommandContextNew context, int argIndex) {
        try {
            LogController.warning("LOCATION TRUE");
            Double.parseDouble(context.args[argIndex]);
            return true;
        } catch (Exception e) {
            LogController.warning("LOCATION FALSE" + context.args[argIndex]);
            return false;
        }
    }

    @Override
    public List<String> tabComplete(CommandContextNew context) {
        return CommandParamNew.super.tabComplete(context); //todo implement!
    }
}