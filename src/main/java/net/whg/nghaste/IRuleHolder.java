package net.whg.nghaste;

/**
 * This interface is used for components which are added to an environment,
 * which have requirements on what other axioms or heurisitics are required for
 * the component to operate properly.
 */
public interface IRuleHolder
{
    static final IAxiom[] EMPTY_AXIOMS = new IAxiom[0];
    static final IHeuristic[] EMPTY_HEURISTICS = new IHeuristic[0];

    /**
     * Gets a list of additional axioms to add to the environment that are required
     * by this component. The default implementation returns an empty array. This
     * method is called during the initialization of the environment with all
     * resulting axioms being added to the environment if not already present in the
     * environment.
     * 
     * @return An array of axioms that are required by this component to work
     *     properly.
     */
    default IAxiom[] getRequiredAxioms()
    {
        return EMPTY_AXIOMS;
    }

    /**
     * Gets a list of additional solution axioms to add to the environment that are
     * required by this component. The default implementation returns an empty
     * array. This method is called during the initialization of the environment
     * with all resulting axioms being added to the environment if not already
     * present in the environment.
     * 
     * @return An array of solution axioms that are required by this component to
     *     work properly.
     */
    default IAxiom[] getRequiredSolutionAxioms()
    {
        return EMPTY_AXIOMS;
    }

    /**
     * Gets a list of additional heuristics to add to the environment that are
     * required by this component. The default implementation returns an empty
     * array. This method is called during the initialization of the environment
     * with all resulting heuristics being added to the environment if not already
     * present in the environment.
     * 
     * @return An array of heuristics that are required by this component to work
     *     properly.
     */
    default IHeuristic[] getRequiredHeuristics()
    {
        return EMPTY_HEURISTICS;
    }
}
