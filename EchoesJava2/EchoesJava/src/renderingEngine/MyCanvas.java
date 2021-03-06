package renderingEngine;

import java.io.File;
import java.net.URL;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class MyCanvas extends GLCanvas implements GLEventListener 
{
  private static final long serialVersionUID = 1L;
  private FPSAnimator animator;
  private GLU glu;
  private Texture background;
  
  public MyCanvas(int width, int height, GLCapabilities capabilities)  
  { 
    super(capabilities);
    setSize(width, height);
    addGLEventListener(this);
  }
  
  @Override
  public void dispose(GLAutoDrawable drawable)
  {
  }
  
  public void init(GLAutoDrawable drawable)
  {
    glu = new GLU();
    
    GL gl = drawable.getGL();
    gl.glClearColor(0f, 0f, 0f, 1f);
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glClearDepth(1.0f);  
    gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  
    gl.glEnable(GL.GL_DEPTH_TEST); 
    gl.glDepthFunc(GL.GL_LEQUAL); 
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
    
    try 
    {
      URL url =  ClassLoader.getSystemResource("renderingEngine/visual/images/GardenBackExplore.png");
      File file = new File(url.toURI());
      background = TextureIO.newTexture(file, true);
    }
    catch (Exception e) { System.out.println(e.toString()); }
    
    // Start animator (which should be a field).
    //animator = new FPSAnimator(this, 60);
    //animator.start();*/
  }
 
  
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
  {
    final GL gl = drawable.getGL();

    if (height <= 0) // avoid a divide by zero error!
      height = 1;
    final float h = (float) width / (float) height;
    gl.glViewport(0, 0, width, height);
    gl.getGL2().glMatrixMode(GL2.GL_PROJECTION);
    gl.getGL2().glLoadIdentity();
    glu.gluPerspective(45.0f, h, 1.0, 20.0);
    gl.getGL2().glMatrixMode(GL2.GL_MODELVIEW);
    gl.getGL2().glLoadIdentity();
  }
  
  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) 
  {
  }
  
  public void display(GLAutoDrawable gLDrawable) 
  {
    final GL gl = gLDrawable.getGL();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    GL2 gl2 = gl.getGL2();
    
    setCamera(gl2, glu, 2);
    drawBackground(gl2);
  }
  
  private void setCamera(GL2 gl, GLU glu, float distance) 
  {
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();
    float widthHeightRatio = (float) getWidth() / (float) getHeight();
    glu.gluPerspective(45, widthHeightRatio, 1, 20);
    glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);
    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glLoadIdentity();
  }
  
  private void drawBackground(GL2 gl) 
  {
    gl.glPushMatrix();
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
    background.enable(gl);
    background.bind(gl);
    gl.glDisable(GL.GL_DEPTH_TEST);
    gl.glColor4f(1,1,1,1);
    gl.glScalef(1, 1, 1);
    gl.glBegin(GL2.GL_QUADS);
    float widthHeightRatio = (float) getWidth() / (float) getHeight();
    gl.glTexCoord2d(0.0,0.0); gl.glVertex2d(-widthHeightRatio,-1);
    gl.glTexCoord2d(1.0,0.0); gl.glVertex2d(widthHeightRatio,-1);
    gl.glTexCoord2d(1.0,1.0); gl.glVertex2d(widthHeightRatio,1);
    gl.glTexCoord2d(0.0,1.0); gl.glVertex2d(-widthHeightRatio,1);
    gl.glEnd();        
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDisable(GL.GL_TEXTURE_2D);
    gl.glPopMatrix();
  }
}

