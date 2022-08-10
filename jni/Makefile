SYSTEM	= linux
JAVADIR	= $$JAVA_HOME/include

CC	= gcc
CFLAGS	= -Wall -I$(JAVADIR) -I$(JAVADIR)/$(SYSTEM)
LD	= gcc
LDFLAGS	=
LIBS	= -L../../../xmp/lib -lxmp
OBJS	= interface.o
SHLIB	= libxmp-jni.so
DYLIB	= libxmp-jni.dylib
DLL	= libxmp-jni.dll

.SUFFIXES: .c .o .lo .a .so .dll 

LOBJS = $(OBJS:.o=.lo)

.c.o:
	$(CC) -c $(CFLAGS) -o $*.lo $<

.c.lo:
	$(CC) -c $(CFLAGS) -fPIC -o $*.lo $<


shared: $(SHLIB)

dylib: $(DYLIB)

dll: $(DLL)


$(SHLIB): $(LOBJS)
	$(LD) $(LDFLAGS) -shared -o $@ $(LOBJS) $(LIBS)

$(DLL): $(OBJS)
	$(LD) $(LDFLAGS) -shared -Wl,--output-def,lib/libxmp-jni.def,--out-implib,lib/libxmp-jni_dll.a -o $@ $(OBJS) $(LIBS)

$(DYLIB): $(LOBJS)
	$(LD) $(LDFLAGS) -dynamiclib -o $@ $(LOBJS) $(LIBS)

clean:
	rm -f core *~ $(LOBJS) $(SHLIB) $(DYLIB) $(DLL)

