//�ǂ̎R���番�z���Ă��邩�̊m�����o���v���O����
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.math.BigDecimal;


public class examination6 {
	public static void main(String[] args) {
		 Connection con = null;

	     PreparedStatement ps = null;
	   
	     try {

	         // �h���C�o�N���X�����[�h
	         Class.forName("com.mysql.jdbc.Driver");

	         // �f�[�^�x�[�X�֐ڑ�
	         con = DriverManager.getConnection("jdbc:mysql://localhost/recruit","root","1108");

	         // name,bloodType,age�̃f�[�^����������SQL�����쐬
	         String str = "\"2012-03-01\"";//����
	         int bunri =2; //(1-���n 2-���n)
	         int gakushu = 2;//(1-���m 2-�C�m 3-�w�� 4-�Z�� 5-���w�Z 6-����)
	         int cluster = 5;//�N���X�^�[�ԍ�
	         //String gakko = "\"���{��w\"";
	         //String sql = "select * from meziro_gakubu_rb where date <"+ str; 
	         //String sql = "SELECT last_date FROM all_retire_students WHERE bunri ="+bunri+" and gakushu ="+ gakushu +" and gakko_name =" + gakko; //
	         //System.out.println(sql);
	         //String sql = "SELECT * FROM all_retire_students"; 
	         String sql;
	        
	         //sql = "SELECT * FROM examination5 WHERE cluster="+cluster; //�w��
	         sql = "SELECT * FROM examination5_rikei WHERE cluster="+cluster; //�C�m�E���n
	       
	         //System.out.println("�ڑ���");
	         // �X�e�[�g�����g�I�u�W�F�N�g�𐶐�
	         ps = con.prepareStatement(sql);
	      
	         //System.out.println("�ڑ���2");
	         // �N�G���[�����s���Č��ʃZ�b�g���擾
	         ResultSet rs = ps.executeQuery();
	         
	         
	         //�ϐ���`1
	         ArrayList<String> old_data = new ArrayList<String>();	//�f�[�^�x�N�g��
	         ArrayList<String> gakko_name = new ArrayList<String>();	//�f�[�^�x�N�g��
	         ArrayList<String> data = new ArrayList<String>();	//�f�[�^�x�N�g��
	         String gakko = "����c��w��w�@";
	         
	         //�������ꂽ�s�������[�v
	         while(rs.next()) {
	         	//�f�[�^�̎擾
	         	old_data.add(rs.getString("last_date"));
	         	gakko_name.add(rs.getString("gakko_name"));
	         
	         }
	         //�f�[�^�x�[�X�̃`�F�b�N
	         for(int i =0;i<old_data.size();i++){
	        	 //System.out.println(data.get(i));
	        	//if(gakko_name.get(i).equals(gakko)){		//�w�Z�������K�v�Ȃ��ꍇ�͂������R�����g
	        		 //System.out.println(gakko_name.get(i));
	        		 //System.out.println(old_data.get(i));
	        		 //System.out.println(old_data.get(i));
	        		 data.add((old_data.get(i)));
	        	//}
	        	 
	         }
	         
	         //���t�̎擾�i2011/12/1���牽���o�߂������j
	         String date2[] = new String[data.size()];
	         String date3[] = new String[data.size()];
	         String date4[] = new String[data.size()];
	         Integer date11[] = new Integer[data.size()];
	         Integer date12[] = new Integer[data.size()];
	         Integer date13[] = new Integer[data.size()];
	         Double last_date[] = new Double[data.size()];
	         String date7;
	         String date8;
	            for(int s=0; s < data.size(); s++){
	            	String date5 = data.get(s);
	            	String date6 = date5.substring(0,4);
	            	String data9 = date5.substring(5,6);//�`�F�b�N�p
	            	if(data9.equals("0")){
	            		date7 = date5.substring(6,7);
	            	} else {
	            		date7 = date5.substring(5,7);
	            	}
	            	String data10 = date5.substring(8,9);//�`�F�b�N�p
	            	if(data10.equals("0")){
	            		date8 = date5.substring(9,10);
	            	} else {
	            		date8 = date5.substring(8,10);
	            	}
	            	
	            	date2[s] = date6;
	            	date3[s] = date7;
	            	date4[s] = date8;
	            	
	            	date11[s] = Integer.parseInt(date2[s]);
	            	date12[s] = Integer.parseInt(date3[s]);
	            	date13[s] = Integer.parseInt(date4[s]);
	            	
	            	//System.out.println("�N;"+date11[s]);
	            	//System.out.println("��;"+date12[s]);
	            	//System.out.println("��;"+date13[s]);
	            	if(date11[s]==2010){ //2012�N�x�ł�2010
	            		last_date[s] = (double)date13[s];
	            	}
	            	else if(date11[s]==2011){ //2012�N�x�ł�2011
	            		last_date[s] = (double)30*date12[s]+date13[s];
	            	}
	            	else {
	            		last_date[s] = (double)365+30*date12[s]+date13[s];
	            	}
	            	//System.out.println(last_date[s]);
	            }

	         
	         //�ϐ���`2
	         int n = data.size();//�f�[�^��
	         int k = 3;//�N���X��
	         int loop = 100000;//�ő�J��Ԃ���
	         double epsilon = 0.01;//1e-2;//��������
	         boolean error = true;
	         double w[][] = new double[k][n];//�����m��
	         double w0[][] = new double[k][n];//��r�p�����m��
	         double ave[] = new double[k];//����
	         double var[] = new double[k];//���U
	         double hensa[] = new double[k];//�W���΍�
	         double num[] = new double[k];//�e�N���X�̃f�[�^
	         double pai[] = new double[k];//�e�N���X�̃f�[�^�̊���
	         double sum_w;//�����m���̘a
	         double sum_w_data;//�����m���ƃf�[�^�̐ς̘a
	         double sum_w_var;//�����m���ƃf�[�^�̕��U�̐ς̘a
	         double sum_num=0.0;//sum�̍��v
	         double sum_p;//�f�[�^�̔����m���̘a
	         int repeat =0;//��������
	         int count_class[] = new int[k];//�e�N���X�ɏ�������f�[�^�̌�
	         int last_count=0;//���[�v��
	         System.out.println("�f�[�^���G"+n);
	        
	         //Step1�@�����l�̑��
	         
	         //for(int s=0; s<k; s++){
	        	 for(int t=0; t<n; t++){
	        		//w[s][t] = 1.0/k; 
	        		 if(last_date[t] <180){
	        			 w[0][t] = 1.0;
	        			 w[1][t] = w[2][t] =0.0;
	        		 } else if(last_date[t]>=180 && last_date[t]<300 ) {
	        			 w[1][t] =1.0;
	        			 w[0][t] = w[2][t] =0.0;
	        		 } else {
	        			 w[2][t] =1.0;
	        			 w[0][t] = w[1][t] =0.0;
	        		 }
	        		 //w[0][t] = 0.3;
	        		 //w[1][t] = 0.7;
	        		 //w[1][t] = 1.0-w[0][t];
	        		//System.out.println(w[s][k]);
	        	 }
	       //  }
	         /*
	         for(int t=0; t<n; t++){
	        	 w[0][t] = (Math.random());
	        	 while(true){
	        		 w[1][t] = (Math.random());
	        		 if(w[0][t] + w[1][t] <1){
	        			 break;
	        		 }
	        	 }
	        	 
	        	 w[2][t] = 1.0-(w[0][t]+w[1][t]);
	         }*/
	         
	         for(int s=0; s<k; s++){
	        	 for(int t=0; t<n; t++){
	        		 //System.out.println("�����Gw["+s+"]["+t+"]"+w[s][t]);
	        		// System.out.print(w[s][t]+",");
	        		 
	        	 }
	        	 //System.out.println();
	         }
	         
	         //Step2 �p�����[�^�v�Z�i��������J��Ԃ��j
	         //�����p�����[�^�̌v�Z
	         for(int kuri=0; kuri<loop; kuri++){
	        	 if(repeat > n*k/2){
	        		 System.out.println("���Ⴉ");
		        	 error = false;
		        	 last_count = kuri;
		        	 break;
		         }
	        	 
	        	 
	         for(int s=0; s<k; s++){
	        	 for(int t=0; t<n; t++){
	        		 w0[s][t] = w[s][t];
	        		 //System.out.print(w[s][t]+",");
	        		//System.out.println("�J��Ԃ�;"+kuri+"w["+s+"]["+t+"]"+w[s][t]);
	        	 }
	        	 //System.out.println();
	         }
	         
	         sum_num=0.0;
	         for(int s=0; s<k; s++){
	        	 sum_w=0.0;
	        	 sum_w_data=0.0;
	        	
	        	 for(int t=0; t<n; t++){
	        		 sum_w = sum_w + w[s][t];
	        		 sum_w_data = sum_w_data +w[s][t]*last_date[t];
	        		 //System.out.println(w[s][t]);
	        		 //System.out.println(data.get(t)*w[s][t]);
	        		 //System.out.println(sum_w_data);
	        	 }
	        	 
	        	 num[s] = sum_w;
	        	 ave[s] = sum_w_data/num[s];
	        	 sum_num=sum_num + num[s];
	        	 //System.out.println(num[s]);
	        	 //System.out.println(sum_w_data);
	        	 //System.out.println(ave[s]);
	         }
	         

	         for(int i=0; i<k; i++){
	        	 pai[i] = num[i]/sum_num;
	        	 
	        	 System.out.println("�J��Ԃ�;"+kuri+"�N���X��"+pai[i]);
	         }
	         
	         
	         //���C�u�����z�p�����[�^�̌v�Z
	         //�ϐ���`
	         int loop2 = 1000000000;//�ő�J��Ԃ���
	         double epsilon2 = 0.01;//��������
	         double a = 1.0;//�����l
	         double m = 1.0;//�����l
	         double scale[] = new double[k];
	         double shape[] = new double[k];
	         boolean error2 = true;
	         
	         //int n = data.size();//�f�[�^��
	         double temp1[] = new double[data.size()];
	         double temp2[] = new double[data.size()];
	         double temp3[] = new double[data.size()];
	         double sum_temp1 = 0.0;
	         double sum_temp2 = 0.0;
	         double sum_temp3 = 0.0;
	         double sum_temp1_2 = 0.0;
	         
	         
	         for(int t=0; t<k; t++) {	
	        	 a = 1.0;//�����l
		         m = 1.0;//�����l
	         
	         for(int i=0; i<loop2; i++){
	        	 double a0 = a;
	        	 double m0 = m;
	        	    //System.out.println(a);
	        	    //System.out.println(m);
	        	 sum_temp1=0.0;
	        	 sum_temp2=0.0;
	        	 sum_temp1_2=0.0;
	        	 for(int s=0; s<n; s++){
	        		 temp1[s] = w[t][s]*Math.pow(last_date[s],m0);
	        		 temp2[s] = w[t][s]*Math.log(last_date[s]);
	        		 //System.out.println("m;"+m0);
	        		 //System.out.println("t;"+Math.pow(last_date[s],m0));
	        		 //System.out.println("t1"+temp1[s]);
	        		 //System.out.println("t2"+temp2[s]);
	        		 sum_temp1 = sum_temp1 + temp1[s];
	        		 sum_temp2 = sum_temp2 + temp2[s];
	        		 sum_temp1_2 = sum_temp1_2 + ((temp1[s]*temp2[s]));
	        	 }
	        	
	        	 //System.out.println("temp1;"+sum_temp1);
	        	 //System.out.println("temp2;"+sum_temp2);
	        	 //System.out.println("temp1_2;"+sum_temp1_2);
	        	 a = num[t]/sum_temp1;
	        	 m = num[t]/(a*sum_temp1_2-sum_temp2);
	        	 //System.out.println("a;"+a);
	        	 //System.out.println("a0;"+a0);
	        	 //System.out.println("m;"+m);
	        	 //System.out.println("m0;"+m0);
	        	
	        	 if(Math.abs(a-a0)<epsilon2 &&Math.abs(m0-m)<epsilon2){
	        		 error2= false;
	        		 break;
	        	 }
	        	 //System.out.println("m;"+m);
		         //System.out.println("m0;"+m0);
	         }
	        
	       if(error2){
	    	   System.out.println("�p�����[�^�̒l���������܂���ł���");
	       }
	       
	       scale[t] = Math.pow((1/a),(1/m));
	       shape[t] = m;
	       System.out.println("�X�P�[���G"+scale[t]);
	       System.out.println("�V�F�C�v�G"+shape[t]);
	 
	        }
	                 
	         //Step3 �����m���̍X�V
	         for(int s=0; s<k; s++){
	        	 for(int t=0; t<n; t++){
	        		 sum_p =0.0;
	        		for(int u=0; u<k; u++){
	        			//sum_p= sum_p+pai[u]*(1-Math.exp(-(Math.pow(last_date[t]/scale[u],shape[u] ))));
	        			sum_p= sum_p+pai[u]*((shape[u]/scale[u])*Math.pow((last_date[t]/scale[u]),(shape[u]-1))*Math.exp(-(Math.pow((last_date[t]/scale[u]), shape[u]))));
	        		}
	        		w[s][t]= (pai[s]*((shape[s]/scale[s])*Math.pow(last_date[t]/scale[s],shape[s]-1)*Math.exp(-(Math.pow(last_date[t]/scale[s], shape[s])))))/sum_p;
	        		//w[s][t]= (pai[s]*(1-Math.exp(-(Math.pow(last_date[t]/scale[s],shape[s])))))/sum_p;
	        		//System.out.println("�J��Ԃ�;"+kuri+"w["+s+"]["+t+"]"+w[s][t]);
	        	 }
	         }
	         
	         
	         //System.out.println("w[s][t]"+w[s][t]);
	         //�I�������t�F�C�Y
	         repeat=0;
	         for(int s=0; s<k; s++){
	        	 for(int t=0; t<n; t++){
	        		 if(Math.abs(w[s][t]-w0[s][t])<epsilon){
	        		 repeat = repeat + 1;
	        		 } 
	        	 }
	         }
	         System.out.println("���s�[�g�G"+repeat);
	         /*if(repeat == n*k/3){
	        	 error = false;
	        	 last_count = kuri;
	        	 break;
	         }*/
	         	         
	         }
	         
	         for(int t=0; t<n; t++){
	        	 if(w[0][t]>0.33){
	        		 count_class[0] = count_class[0]+1;
	        	 } else if(w[1][t]>0.33) {
	        		 count_class[1] = count_class[1]+1;
	        	 } else {
	        		 count_class[2] = count_class[2]+1;
	        	 }
	         }
	         
	         System.out.println("�J��Ԃ���;"+last_count);
	         //���ʂ̕\��
	         if(error) {
	        	 System.out.println("�������܂���ł����D");
	         }
	         //System.out.println("�f�[�^,�N���X,�m��");
	         for(int s=0; s<k; s++){
	        	 for(int t=0; t<n; t++){
	        		//System.out.println("�f�[�^"+t+"���N���X"+s+"�ɏ�������m���G"+w[s][t]);
	        		 //System.out.println(t+","+s+","+w[s][t]);
	        	 }
	         }
	         //System.out.println("�f�[�^,�����N���X");
	         for(int t=0; t<n; t++){
	        	 if(w[0][t]>0.33){
	        		 //System.out.println(t+",0");
	        	 } else if(w[1][t] >0.33){
	        		 //System.out.println(t+",1");
	        	 } else {
	        		 //System.out.println(t+",2");
	        	 }
	         }
	         
	         for(int s=0; s<k; s++){
	        	 System.out.println("�N���X"+s+"�ɏ�������f�[�^�̌�;"+count_class[s]);
	        	 System.out.println("�N���X"+s+"�ɑ΂���d��;"+(double)count_class[s]/(double)n);
	         }
	        
	         
	         
	     } catch (SQLException e) {
	         e.printStackTrace();
	     } catch (ClassNotFoundException e) {
	         e.printStackTrace();
	     } finally {
	         try {

	             // close����
	             if(ps != null){
	                 ps.close();
	             }
	             
	             // close����
	             if(con != null){
	                 con.close();
	             }
	         } catch(SQLException e){
	             e.printStackTrace();
	         }
	     }
	 }
}