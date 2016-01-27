/* Command interface */
/* Author: Wei Zhang 
Latest Update: Jan 20, 2016 */
/* Controller pattern*/
/* For encapsulating a method and passing it to another */

/* API
public interface Command {
    public void execute(Object data);
}
*/
package org.dharmatech.controllers;

public interface Command {
    public void execute(Object data);
}