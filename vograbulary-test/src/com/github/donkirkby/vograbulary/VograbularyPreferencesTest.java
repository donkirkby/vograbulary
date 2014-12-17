package com.github.donkirkby.vograbulary;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

//import com.badlogic.gdx.Preferences;

public class VograbularyPreferencesTest {
//    private Preferences raw;
//    private VograbularyPreferences preferences;
//
//    @Before
//    public void setUp() {
//        raw = mock(Preferences.class);
//        preferences = new VograbularyPreferences(raw);
//    }
//    
//    @Test
//    public void studentNames() {
//        String expectedNames = "Alex|Betty";
//        String expectedName1 = "Alex";
//        String expectedName2 = "Betty";
//        when(raw.getString(anyString(), anyString())).thenReturn(expectedNames);
//
//        preferences.setStudentNames(expectedName1, expectedName2);
//        verify(raw).putString(anyString(), eq(expectedNames));
//        
//        String name1 = preferences.getStudentNames().get(0);
//        String name2 = preferences.getStudentNames().get(1);
//        
//        assertThat("name 1", name1, is(expectedName1));
//        assertThat("name 2", name2, is(expectedName2));
//    }
//    
//    @Test
//    public void studentNamesShort() {
//        String expectedNames = "A|B";
//        String expectedName1 = "A";
//        String expectedName2 = "B";
//        when(raw.getString(anyString(), anyString())).thenReturn(expectedNames);
//
//        preferences.setStudentNames(expectedName1, expectedName2);
//        verify(raw).putString(anyString(), eq(expectedNames));
//        
//        String name1 = preferences.getStudentNames().get(0);
//        String name2 = preferences.getStudentNames().get(1);
//        
//        assertThat("name 1", name1, is(expectedName1));
//        assertThat("name 2", name2, is(expectedName2));
//    }
//    
//    @Test
//    public void containsPipe() {
//        String expectedNames = "Al/ex|Bet/ty";
//        String expectedName1 = "Al/ex";
//        String expectedName2Input = "Bet|ty";
//        String expectedName2Output = "Bet/ty";
//        when(raw.getString(anyString(), anyString())).thenReturn(expectedNames);
//
//        preferences.setStudentNames(expectedName1, expectedName2Input);
//        verify(raw).putString(anyString(), eq(expectedNames));
//        
//        String name1 = preferences.getStudentNames().get(0);
//        String name2 = preferences.getStudentNames().get(1);
//        
//        assertThat("name 1", name1, is(expectedName1));
//        assertThat("name 2", name2, is(expectedName2Output));
//    }
}
