package com.exemplar.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.exemplar.entity.Product;
import com.exemplar.entity.User;

public class InterviewExamples {
	
	public void evaluateStreams() {

		List<User> users=new ArrayList<>();
		List<Product> products=new ArrayList<>();
		
		List<User> filtered = users.stream().filter(u -> u.getEmail()!=null).toList();
		
		List<User> filtered2 = users.stream().filter(u -> u.getEmail()!=null).collect(Collectors.toList());
		
		
		long count = products.stream().map(p -> p.getQuantity()).count();
		
		long totalProductsCost = products.stream().map(p -> p.getPrice()).limit(10).reduce(0, Integer::sum);
		
		Map<String, Long> groupByCount = Stream.of("venu","gopal", "reddy").filter(Objects::nonNull).collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
		
		IntStream.range(2, 10).forEach(System.out::println);
		
	}
	
	public void seeeCollections() {
		
		List<User> users=new ArrayList<>();
		Set<User> usersSet=new HashSet();
		
		Collections.sort(users, (u1,u2) -> u1.getFullName().compareTo(u2.getEmail()) );
	
		Collections.binarySearch(users, new User(), (u1,u2) -> u1.getFullName().compareTo(u2.getEmail()));
		
		Collections.reverse(users);
		
		List<User> unmodifiableList = Collections.unmodifiableList(users);
		
		List<User> users2=new ArrayList<>();
		
		Collections.copy(users2, users);
		
		Collections.disjoint(users, users2);
		
		List<?> temp=Collections.EMPTY_LIST;
		
		List<User> synchronizedList = Collections.synchronizedList(users);
		Set<User> synchronizedSet = Collections.synchronizedSet(usersSet);
		
	}
	
	public void arrayMethods() {
		
		List<Integer> asList = Arrays.asList(1,2,3);
		int[] array=new int[]{1,2,3 };
		
		Arrays.stream(array).boxed().collect(Collectors.toList());
		
		List<User> users=new ArrayList<>();
		
		
		int[] tempArray=new int[]{10,5,6,3, 7,1};
		
		Arrays.sort(tempArray);
		Arrays.binarySearch(tempArray, 6);
		int compare = Arrays.compare(array, tempArray);
		
		String string = Arrays.toString(tempArray);
		
		Arrays.copyOfRange(tempArray, 1, 3);
		
		Arrays.fill(tempArray, 2);
		
		
	}
	
	
}


