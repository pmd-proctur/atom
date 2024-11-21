package com.proctur.atom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class Extras {
	
		private String udf1;
		private String udf2;
		private String udf3;
		private String udf4;
		private String udf5;
		private String udf9;
		@Override
		public String toString() {
			return "Extras [udf1=" + udf1 + ", udf2=" + udf2 + ", udf3=" + udf3 + ", udf4=" + udf4 + ", udf5=" + udf5
					+ ", getUdf1()=" + getUdf1() + ", getUdf2()=" + getUdf2() + ", getUdf3()=" + getUdf3()
					+ ", getUdf4()=" + getUdf4() + ", getUdf5()=" + getUdf5() + ", getClass()=" + getClass()
					+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
		}
	
	

}
