This is Serialysis, a library to parse serialized Java objects.
It was originally published in [this blog entry](https://community.oracle.com/blogs/emcmanus/2007/06/12/disassembling-serialized-java-objects).

Thanks to Chris Frohoff (frohoff@) for rescuing the source code from oblivion. It had not survived
the migration of blogs from `weblogs.java.net` to `community.oracle.com`.

The text below is closely based on the original blog entry, dating from
12 June 2007.

---------------

Presenting Serialysis, a library that allows you to disassemble the
serial form of Java objects. This can allow you to retrieve information
about an object that is not available through its public API. It
is also a useful tool when testing the serialization of your classes.

## When the public API is not enough

My reason for writing this library is that I encountered a couple
of problems where I found that I needed information from an object
that was not available through its public API, but that *was*
available through its serial form.

One example
is if you have a stub for a remote RMI object, and you want to know
what address it will connect to, or what port, or using what
[socket factory](http://java.sun.com/javase/6/docs/api/java/rmi/server/RMIClientSocketFactory.html)
The standard RMI API doesn't
give you any way to extract this information from the stub. But the
information is there, and it must be included when the stub is
serialized so that the stub is usable when it is later deserialized.
So if we could somehow parse the serialized stub we could get the
information we want.

A second example comes from the
[JMX API](http://java.sun.com/javase/6/docs/api/javax/management/package-frame.html).
Queries to the MBean Server are represented by the interface
[`QueryExp`](http://java.sun.com/javase/6/docs/api/javax/management/QueryExp.html).
`QueryExp` instances are constructed
using the methods of the
[`Query`](http://java.sun.com/javase/6/docs/api/javax/management/Query.html)
class. If you have an object implementing
`QueryExp`, how can you know what query it executes? The JMX API
doesn't include any method to find out. The information must be
present in the serial form, so that when a client sends a query to
a remote server it can be reconstituted on the server. If we could
look at the serial form, we could find out what the query
was.

This second example is what prompted me to
write this library. The existing standard JMX connectors are based
on Java serialization, so they don't need to do anything special
for `QueryExp`s. But the new
[Web Services Connector](https://ws-jmx-connector.dev.java.net/)
being defined by [JSR 262](http://jcp.org/en/jsr/detail?id=262)
uses XML for serialization. How can it analyze a `QueryExp` in order
to convert it into XML? The answer is that the WS Connector uses a
version of this library to look at the Java-serialized
`QueryExp`.

What these examples have in common is
that they illustrate gaps in the relevant APIs. There *ought*
to be methods that allow you to extract the information contained
in an RMI stub. There *ought* to be methods that convert back
from a `QueryExp` object to the original `Query` methods that constructed
it. (Even a standardized parseable `toString()` would be enough.) But
those methods aren't there today, and if we want code that works
with those APIs as they are now, we need another
approach.

## Grabbing the private fields of objects

If you have the source code of the
classes you're interested in, it's tempting just to barrel in and
grab the information you need. In the RMI stub example, we can find
out by experiment that the stub's
[`getRef()`](http://java.sun.com/javase/6/docs/api/java/rmi/server/RemoteObject.html#getRef())
method returns
a `sun.rmi.server.UnicastRef`, and by studying the JDK
source we might be able to figure out that this class contains a
field `ref` of type `sun.rmi.transport.LiveRef`
with the information we need. So we might end up with code like this:

```java
// This is NOT a good idea!!!

import sun.rmi.server.*;     // !
import sun.rmi.transport.*;  // !
import java.rmi.*;
import java.rmi.server.*;    // !

public class StubDigger {
    public static getPort(RemoteStub stub) throws Exception {
        RemoteRef ref = stub.getRef();
        UnicastRef uref = (UnicastRef) ref;                         // !
        Field refField = UnicastRef.class.getDeclaredField("ref");  // !
        refField.setAccessible(true);                               // !
        LiveRef lref = (LiveRef) refField.get(uref);                // !
        return lref.getPort();
    }
}
```

You might be satisfied with this, but you shouldn't be. The lines
marked `// !` are full of horrors. First of all, you should
*never* depend on `sun.*` classes, because there's
no guarantee they won't change unrecognizably in any JDK update,
plus of course your code probably won't be portable to platforms
other than the JDK. Secondly, it's a huge red flag when you see
[Field.setAccessible](http://java.sun.com/javase/6/docs/api/java/lang/reflect/AccessibleObject.html#setAccessible(java.lang.reflect.AccessibleObject{},%20boolean))
being called. That means the
code is depending on undocumented fields, which again could change
between releases, or, worse, which might continue to exist but with
subtly different semantics.

(The above code was
written for JDK 5. It turns out that in JDK 6, LiveRef acquires a
public `getPort()` method, so you no longer need `Field.setAccessible`.
But you still need to depend on `sun.*`
classes.)

Well, sometimes you can't do any better
than this. But if the class you're interested in is serializable,
often you can. The reason is that the serial form of a class is
part of its public interface. If the API is any good at all then
its public interfaces will evolve compatibly in every update. This
is a very strong requirement on the JDK platform in
particular.

So if the information you need isn't
available through a class's public methods, but *is* part
of the documented serial form, then you can rely on it remaining
in the serial form in the future.

The serial form is included in the Javadoc output as part of the *See
Also* for each serializable class. You can see the serial
form of all public JDK classes in [a single giant
page](http://java.sun.com/javase/6/docs/api/serialized-form.html).

## Enter Serialysis

My library to parse serialized objects
is called *Serialysis*, the result of cramming the words
"serial analysis" too close together.

Here's a
simple example of what it looks like in action. This
code...

```java
SEntity sint = SerialScan.examine(new Integer(5));
System.out.println(sint);
```

...produces this output...

```
SObject(java.lang.Integer){
  value = Prim(int){5}
}
```

This tells us that the `java.lang.Integer` that we gave to
`SerialScan.examine` serializes as an object with a
single field `value` of type `int`. If we
check out the [documented serialized form of
`java.lang.Integer`](http://java.sun.com/javase/6/docs/api/serialized-form.html#java.lang.Integer)
we can see that this is indeed what is expected.

If you check out the source code of `java.lang.Integer`, you'll see that the class
itself also has a single field `value` of
type `int`:

```java
    /**
     * The value of the &lt;code&gt;Integer&lt;/code&gt;.
     *
     * @serial
     */
    private final int value;
```
	
But private fields are an implementation
detail. An update could rename this field, or replace it with a new
field inherited from the parent class
[`java.lang.Number`](http://java.sun.com/javase/6/docs/api/java/lang/Number.html),
or whatever.
There's no guarantee that that won't happen, but there *is*
a guarantee that the serial form will remain the same. Serialization provides
[mechanisms](http://java.sun.com/javase/6/docs/platform/serialization/spec/serial-arch.html#6250")
to keep the serial form the same even
when the class's fields change.

Here's a more
complicated example. Suppose that, for some reason, we want to know
how big the array in an
[`ArrayList`](http://java.sun.com/javase/6/docs/api/java/util/ArrayList.html)
is. The API doesn't allow us to find out, though it does allow us to
[force](http://java.sun.com/javase/6/docs/api/java/util/ArrayList.html#ensureCapacity(int))
the array to be at least a certain size.

If we check the [serial form of
`ArrayList`](http://java.sun.com/javase/6/docs/api/serialized-form.html#java.util.ArrayList),
we see that it does
contain the information we're looking for. There's a serialized
field `size`, which is the number of elements in the list.
That's not what we want. But the *Serial Data* in
the `writeObject` method does have what we want:

<blockquote>
<dl><dt><strong>Serial Data:</strong></dt>
<dd>The length of the array backing the
<code>ArrayList</code>instance is emitted (int), followed by all
of its elements (each an <code>Object</code>) in the proper
order.</dd>
</dd>
</dl>
</blockquote>

If we execute
this code...

```java
    List<Integer> list = new ArrayList<Integer>();
    list.add(5);
    SObject slist = (SObject) SerialScan.examine(list);
    System.out.println(slist);
```
	
...we get this output...

```
SObject(java.util.ArrayList){
  size = SPrim(int){1}
  -- data written by class's writeObject:
  SBlockData(blockdata){4 bytes of binary data}
  SObject(java.lang.Integer){
    value = SPrim(int){5}
  }
}
```

This is where we get into the gory details of serialization. In
addition to, or instead of, serializing an object's fields, its
class can declare a method `writeObject(ObjectOutputStream)`
that writes arbitrary data to the serial stream using methods like
[`ObjectOutputStream.writeInt`](http://java.sun.com/javase/6/docs/api/java/io/ObjectOutputStream.html#writeInt(int))
It must declare a corresponding `readObject` that reads the same data, and
it should document via a
[`@serialData`](http://java.sun.com/j2se/1.5.0/docs/tooldocs/solaris/javadoc.html#@serialData)
tag what the `writeObject` method writes, as `ArrayList` does.

The `writeObject` data is accessible in Serialysis through
the method `SObject.getAnnotations()`, which returns a
`List<SEntity>`. Each `Object` that was written via the method
[`ObjectOutputStream.writeObject(Object)`](http://java.sun.com/javase/6/docs/api/java/io/ObjectOutputStream.html#writeObject(java.lang.Object))
appears as an `SObject` in this list. Each chunk of data written
by one or more consecutive calls to the methods that `ObjectOutputStream` gets from
[`DataOutput`](http://java.sun.com/javase/6/docs/api/java/io/DataOutput.html)
([writeInt](http://java.sun.com/javase/6/docs/api/java/io/ObjectOutputStream.html#writeInt(int)),
[writeUTF](http://java.sun.com/javase/6/docs/api/java/io/ObjectOutputStream.html#writeUTF(java.lang.String)),
etc) appears as an `SBlockData`.
The serial stream doesn't include enough information to separate
out individual items within the chunk; that information is an
agreement between writer and reader that is documented by
the `@serialData` tag.

Based on the `ArrayList` documentation, we can find the size of the array like
this:

```java
    SObject slist = (SObject) SerialScan.examine(list);
    List<SEntity> writeObjectData = slist.getAnnotations();
    SBlockData data = (SBlockData) writeObjectData.get(0);
    DataInputStream din = data.getDataInputStream();
    int alen = din.readInt();
    System.out.println("Array length: " + alen);
```

## How Serialysis solves my example problems

Without showing all the details of the code, here's the outline of
the solution to the *`QueryExp` problem* I mentioned.
Suppose I have a `QueryExp` constructed like this:

```java
QueryExp query =
    Query.or(Query.gt(Query.attr("Version"), Query.value(5)),
         Query.eq(Query.attr("SupportsSpume"), Query.value(true)));
```
		 
This means, "MBeans where the `Version` attribute is greater than
5 or the `SupportsSpume` attribute is true. The `toString()`
of this query in the JDK looks like this:

`((Version) > (5)) or ((SupportsSpume) = (true))`

The result of `SerialScan.examine` looks like this:

```
SObject(javax.management.OrQueryExp){
  exp1 = SObject(javax.management.BinaryRelQueryExp){
    relOp = SPrim(int){0}
    exp1 = SObject(javax.management.AttributeValueExp){
      attr = SString(String){"version"}
    }
    exp2 = SObject(javax.management.NumericValueExp){
      val = SObject(java.lang.Long){
        value = SPrim(long){5}
      }
    }
  }
  exp2 = SObject(javax.management.BinaryRelQueryExp){
    relOp = SPrim(int){4}
    exp1 = SObject(javax.management.AttributeValueExp){
      attr = SString(String){"supportsSpume"}
    }
    exp2 = SObject(javax.management.BooleanValueExp){
      val = SPrim(boolean){true}
    }
  }
}
```

You can imagine code that descends into this structure producing
an XML equivalent. Every conformant implementation of the JMX API
is required to produce this same serial form, so the code that
parses it is guaranteed to work everywhere.

Now here's the code that solves the **RMI stub port number problem**:

```java
public static int getPort(RemoteStub stub) throws IOException {
    SObject sstub = (SObject) SerialScan.examine(stub);
    List<SEntity> writeObjectData = sstub.getAnnotations();
    SBlockData sdata = (SBlockData) writeObjectData.get(0);
    DataInputStream din = sdata.getDataInputStream();
    String type = din.readUTF();
    if (type.equals("UnicastRef"))
        return getPortUnicastRef(din);
    else if (type.equals("UnicastRef2"))
        return getPortUnicastRef2(din);
    else
        throw new IOException("Can't handle ref type " + type);
    }

private static int getPortUnicastRef(DataInputStream din) throws IOException {
    String host = din.readUTF();
    return din.readInt();
}

private static int getPortUnicastRef2(DataInputStream din) throws IOException {
    byte hasCSF = din.readByte();
    String host = din.readUTF();
    return din.readInt();
}
```

To understand this, you need to see the [serial form for
`RemoteObject`](http://java.sun.com/javase/6/docs/api/serialized-form.html#java.rmi.server.RemoteObject).
This code is admittedly difficult, but it is portable and futureproof.
It should be fairly clear how to extract the other information I
mentioned from RMI stubs using the same
approach.

## Conclusions

You really don't want to get into disassembling serial forms unless you
have to. But if you *do* have to, then Serialysis should
make your task a little less painful.

It's also a good way to check that your own classes serialize the way you
expect them to.
