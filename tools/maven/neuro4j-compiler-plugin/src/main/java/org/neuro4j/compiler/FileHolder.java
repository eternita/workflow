/*
 * Copyright (c) 2013-2016, Neuro4j
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

package org.neuro4j.compiler;

import java.io.File;

public class FileHolder {

	private final File file;
	private final String sourceRoot;

	public FileHolder(File file, String sourceRoot) {
		super();
		this.file = file;
		this.sourceRoot = sourceRoot;
	}

	public File getFile() {
		return file;
	}

	public String getSourceRoot() {
		return sourceRoot;
	}

}
