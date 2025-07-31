package edu.HanYi;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@SelectPackages("edu.HanYi")
@IncludeClassNamePatterns(".*Test")
@SpringBootTest
class HanYiApplicationTests {}
