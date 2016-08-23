package edu.casetools.mreasoner.core.control;

import java.util.concurrent.Semaphore;

public class ReasonerSemaphore {
		Semaphore reasoner,input;
		
		public ReasonerSemaphore(boolean enable){
			if(enable){
				reasoner = new Semaphore(0);
				input    = new Semaphore(0);
			}else{
				reasoner = null;
				input    = null;
			}

		}
		
		public void reasonerPut(){
			if(reasoner != null){
				reasoner.release();
			}
		}
		
		public void reasonerTake(){
			try {
				if(reasoner != null){
					reasoner.acquire();

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void inputPut(){
			if(input != null){
				input.release();
			}
		}
		
		public void inputTake(){
			try {
					input.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}
