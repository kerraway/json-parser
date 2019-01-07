package com.github.kerraway.jsonparser;

import com.github.kerraway.jsonparser.tokenizer.TokenizerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author kerraway
 * @date 2019/1/4
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TokenizerTest.class,
})
public class JsonParserTests {
}
