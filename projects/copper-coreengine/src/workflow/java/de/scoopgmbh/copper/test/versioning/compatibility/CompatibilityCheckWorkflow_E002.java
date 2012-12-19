/*
 * Copyright 2002-2012 SCOOP Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.scoopgmbh.copper.test.versioning.compatibility;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.scoopgmbh.copper.InterruptException;
import de.scoopgmbh.copper.WaitMode;
import de.scoopgmbh.copper.WorkflowDescription;
import de.scoopgmbh.copper.persistent.PersistentWorkflow;

/**
 * Incompatible change example E002
 * 
 * adding a new local variable to a directly or indirectly waiting method
 *
 * @author austermann
 *
 */
@WorkflowDescription(alias=CompatibilityCheckWorkflowDef.NAME,majorVersion=1,minorVersion=1,patchLevelVersion=002)
public class CompatibilityCheckWorkflow_E002 extends PersistentWorkflow<Serializable> {
	
	private static final Logger logger = LoggerFactory.getLogger(CompatibilityCheckWorkflow_E002.class);

	private static final long serialVersionUID = 1L;
	
	private String aString;
	private String bString;
	
	@Override
	public void main() throws InterruptException {
		aString = "A";
		int localIntValue = 1;
		directlyWaitingMethod(aString, localIntValue);
		bString = "B";
		localIntValue++;
		indirectlyWaitingMethod(bString, localIntValue);
	}
	
	protected void directlyWaitingMethod(String strValue, int intValue) throws InterruptException {
		Object NEW_LOCAL_VARIABLE = "NEW_LOCAL_VARIABLE";
		neverWaitingMethod(strValue, intValue);
		this.wait(WaitMode.ALL, 500, Long.toHexString(System.currentTimeMillis()));
	}
	
	protected void indirectlyWaitingMethod(String strValue, int intValue) throws InterruptException {
		final Object localObject = 10867L;
		directlyWaitingMethod(strValue, intValue);
		logger.debug("{}", localObject);
	}
	
	protected void neverWaitingMethod(String strValue, int intValue) {
		logger.debug("strValue="+strValue+", intValue="+intValue);
		anotherNeverWaitingMethod(strValue, intValue);
	}
	
	protected void anotherNeverWaitingMethod(String strValue, int intValue) {
		logger.debug("strValue="+strValue+", intValue="+intValue);
	}

}
