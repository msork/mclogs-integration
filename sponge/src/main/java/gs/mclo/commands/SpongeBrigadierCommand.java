package gs.mclo.commands;

import com.mojang.brigadier.CommandDispatcher;
import gs.mclo.components.AdventureComponent;
import gs.mclo.components.AdventureStyle;
import gs.mclo.components.IComponentFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpongeBrigadierCommand implements Command.Raw {
    private BrigadierExecutor<CommandCause, AdventureComponent, AdventureStyle, ClickEvent> executor;
    private CommandDispatcher<CommandCause> dispatcher;

    public SpongeBrigadierCommand(
            CommandDispatcher<CommandCause> dispatcher,
            BuildContext<CommandCause, AdventureComponent> buildContext,
            IComponentFactory<AdventureComponent, AdventureStyle, ClickEvent> componentFactory
    ) {
        this.dispatcher = dispatcher;
        this.executor = new BrigadierExecutor<>(dispatcher, buildContext, componentFactory);
    }

    @Override
    public CommandResult process(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
        String remaining = arguments.remaining().trim();
        String[] args = remaining.isEmpty() ? new String[0] : remaining.split("\\s+");

        executor.executeCommand(cause, "mclogs", args);
        return CommandResult.success();
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
        String remaining = arguments.remaining().trim();
        String[] args = remaining.isEmpty() ? new String[0] : remaining.split("\\s+");

        return executor.completeCommand(cause, "mclogs", args).stream()
                .map(CommandCompletion::of)
                .collect(Collectors.toList());
    }

    @Override
    public boolean canExecute(CommandCause cause) {
        return true;
    }

    @Override
    public Optional<Component> shortDescription(CommandCause cause) {
        return Optional.empty();
    }

    @Override
    public Optional<Component> extendedDescription(CommandCause cause) {
        return Optional.empty();
    }

    @Override
    public Component usage(CommandCause cause) {
        // Sponge does not allow including the alias.
        return Component.text("");
    }
}