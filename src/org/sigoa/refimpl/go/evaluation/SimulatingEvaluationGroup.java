/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-09
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.evaluation.SimulatingEvaluationGroup.java
 * Last modification: 2007-12-09
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to theFree Software
 *                    Foundation, Inc. 51 Franklin Street, Fifth Floor,
 *                    Boston, MA 02110-1301, USA or download the license
 *                    under http://www.gnu.org/licenses/lgpl.html or
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package org.sigoa.refimpl.go.evaluation;

/**
 * A simulating evaluation group.
 * 
 * @author Thomas Weise
 */
public class SimulatingEvaluationGroup {// extends EvaluationGroup {
// /**
// * the serial version uid
// */
// private static final long serialVersionUID = 1l;
//
// /**
// * the static states
// */
// private final Serializable[] m_staticStates;
//
// /**
// * the objective states
// */
// private final IObjectiveState[] m_states;
//
// /**
// * the values
// */
// private final double[][] m_values;
//
// /**
// * the simulation
// */
// private final ISimulation<?> m_simulation;
//
// /**
// * the simulation mangaer
// */
// private final ISimulationManager m_manager;
//
// /**
// * Create an evaluation group by copying another one.
// *
// * @param e
// * the evaluation group
// * @param maxEval
// * the maximum evaluation numbers
// */
// @SuppressWarnings("unchecked")
// protected SimulatingEvaluationGroup(final EvaluationGroup e,
// final int maxEval) {
// super(e);
//
// IObjectiveFunction<?, ?, ?, ?>[] of;
// int i;
// Serializable[] ss;
// IObjectiveState[] os;
//
// this.m_manager = this.getSimulationManager();
// this.m_simulation = this.m_manager.getSimulation(this.m_id);
//
// of = this.m_objectives;
// i = of.length;
// this.m_states = os = new IObjectiveState[i];
// this.m_staticStates = ss = new Serializable[i];
// this.m_values = new double[i][maxEval];
//
// for (--i; i >= 0; i--) {
// os[i] = ((IObjectiveFunction) (of[i])).createState(//
// ss[i] = of[i].createStaticState());
//
// }
// }
//
// /**
// * Perform the <code>index</code>th simulation
// * @param index the simulation index
// */
// protected void simulate(int index){
// IObjectiveFunction<?, ?, ?, ?>[] of;
// Serializable[] ss;
// IObjectiveState[] os;
//    
// of=this.m_objectives;
// 
//    
// }
//  
// /**
// * finalize this evaluator
// *
// * @throws Throwable
// * if needed
// */
// @Override
// protected void finalize() throws Throwable {
// try {
// this.m_manager.returnSimulation(this.m_simulation);
// } finally {
// super.finalize();
// }
// }
}
