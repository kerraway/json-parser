### Json 解析器

---

Json 格式是 1999 年《JavaScript Programming Language, Standard ECMA-262 3rd Edition》的子集合，所以可以在 JavaScript 以 eval() 函数（javascript 通过 eval() 调用解析器）读入。不过这并不代表 Json 无法使用于其他语言，事实上几乎所有与网页开发相关的语言都有 Json 函数库。

Json 用于描述资料结构，有两种结构存在：
- 对象（object）：一个对象包含一系列非排序的名称／值对（pair），一个对象以 `{` 开始，并以 `}` 结束。每个名称／值对之间使用 `:` 分割。
```json
{name1: value1, name2: value2}
```
- 数组（array）：一个数组是一个值（value）的集合，一个数组以 `[` 开始，并以 `]` 结束。数组成员之间使用 `,` 分割。
```json
[value1, value2]
```