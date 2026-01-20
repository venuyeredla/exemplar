package com.exemplar.cache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.exemplar.entity.Item;

@Service
public class RedisMessageSubscriber implements MessageListener {
	
	public static List<String> messageList = new ArrayList<String>();

	@Override
	public void onMessage(Message message, byte[] pattern) {
		
		 ByteArrayInputStream inputStream=new ByteArrayInputStream(message.getBody());
		 try {
			ObjectInputStream in = new ObjectInputStream(inputStream);
			
			 Item item=(Item)in.readObject();
			 messageList.add(message.toString());
		     System.out.println("Message received: and balance is " + item.getBalance());
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
