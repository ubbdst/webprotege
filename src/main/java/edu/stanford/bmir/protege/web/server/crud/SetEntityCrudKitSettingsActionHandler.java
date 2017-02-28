package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.FindAndReplaceIRIPrefixChangeGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.crud.IRIPrefixUpdateStrategy;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.MANAGE_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SetEntityCrudKitSettingsActionHandler extends AbstractHasProjectActionHandler<SetEntityCrudKitSettingsAction, SetEntityCrudKitSettingsResult> {

    @Inject
    public SetEntityCrudKitSettingsActionHandler(OWLAPIProjectManager projectManager,
                                                 AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<SetEntityCrudKitSettingsAction> getActionClass() {
        return SetEntityCrudKitSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return MANAGE_PROJECT;
    }

    @Override
    protected SetEntityCrudKitSettingsResult execute(SetEntityCrudKitSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        project.setEntityCrudKitSettings(action.getToSettings());
        if(action.getPrefixUpdateStrategy() == IRIPrefixUpdateStrategy.FIND_AND_REPLACE) {
            String fromPrefix = action.getFromSettings().getPrefixSettings().getIRIPrefix();
            String toPrefix = action.getToSettings().getPrefixSettings().getIRIPrefix();
            FindAndReplaceIRIPrefixChangeGenerator changeGenerator = new FindAndReplaceIRIPrefixChangeGenerator(fromPrefix, toPrefix);
            project.applyChanges(executionContext.getUserId(), changeGenerator, new FixedMessageChangeDescriptionGenerator<Void>("Replaced IRI prefix <" + fromPrefix + "> with <" + toPrefix + ">"));
        }
        return new SetEntityCrudKitSettingsResult();
    }

}
