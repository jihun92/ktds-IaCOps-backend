package com.ktds.IaCOps.iacengine.ansible.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnsibleModel {

	private String playbookName;
	private String targetHost;

}
