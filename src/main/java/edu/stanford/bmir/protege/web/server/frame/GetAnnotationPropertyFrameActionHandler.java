package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetAnnotationPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetAnnotationPropertyFrameAction, GetAnnotationPropertyFrameResult> {

    @Inject
    public GetAnnotationPropertyFrameActionHandler(OWLAPIProjectManager projectManager,
                                                   AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetAnnotationPropertyFrameResult execute(GetAnnotationPropertyFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        AnnotationPropertyFrameTranslator translator = new AnnotationPropertyFrameTranslator();
        AnnotationPropertyFrame frame = translator.getFrame(action.getSubject(), project.getRootOntology(), project);
        String label = project.getRenderingManager().getBrowserText(action.getSubject());
        LabelledFrame<AnnotationPropertyFrame> labelledFrame = new LabelledFrame<>(label, frame);
        BrowserTextMap btm = BrowserTextMap.build(project.getRenderingManager(), frame.getSignature());
        return new GetAnnotationPropertyFrameResult(labelledFrame, btm);
    }

    @Override
    public Class<GetAnnotationPropertyFrameAction> getActionClass() {
        return GetAnnotationPropertyFrameAction.class;
    }
}
