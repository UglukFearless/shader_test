package net.uglukfearless.shader_test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderTest implements ApplicationListener {
	SpriteBatch batch;
	Texture texture0, texture1, mask, background;
	OrthographicCamera camera;
	ShaderProgram shader;
	SpriteBatch defaultBatch;

	private float delta;
	private float stateTime;
	private Color currentColor;
	
	@Override
	public void create () {
		final String VERTEX = Gdx.files.internal("shader/vert1.glsl").readString();
		final String FRAGMENT = Gdx.files.internal("shader/frag1.glsl").readString();

		// текстура не имеет значения, так как мы будем игнорировать ее в любом случае
		// tex = new Texture (256, 256, Format.RGBA8888);
		texture0 = new Texture(Gdx.files.internal("textures/badlogic.jpg"));
//		texture0 = new Texture(Gdx.files.internal("textures/Logo.png"));
//		texture1 = new Texture(Gdx.files.internal("textures/Logo.png"));
		texture1 = new Texture(Gdx.files.internal("textures/menu.png"));
//		mask = new Texture(Gdx.files.internal("textures/menu.png"));
		mask = new Texture(Gdx.files.internal("textures/lock.png"));

		background = new Texture(Gdx.files.internal("textures/Logo.png"));

		//отключаем стандартный набор атрибутов
		ShaderProgram.pedantic = false;

		shader = new ShaderProgram(VERTEX, FRAGMENT);

		if (!shader.isCompiled()) {
			System.err.println(shader.getLog());
			System.exit(0);
		}

		if (shader.getLog().length()!=0) {
			System.out.println(shader.getLog());
		}

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true);

		shader.begin();
		shader.setUniformi("u_texture1", 1);
		shader.setUniformi("u_mask", 2);
		shader.setUniformf("ourColor", Color.YELLOW);
		GL20 gl = Gdx.graphics.getGL20();

		// сделать GL_TEXTURE2 активным блоком текстуры, затем привязать текстуру маски
		gl.glActiveTexture(GL20.GL_TEXTURE2);
		mask.bind();
		// делаем то же самое для двух других блоков текстуры
		gl.glActiveTexture(GL20.GL_TEXTURE1);
		texture1.bind();
		gl.glActiveTexture(GL20.GL_TEXTURE0);
		texture0.bind();
		shader.end();

		batch = new SpriteBatch(1000, shader);
		batch.setShader(shader);

		defaultBatch = new SpriteBatch();
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render () {
		delta = Gdx.graphics.getDeltaTime();
		stateTime += delta;
		currentColor = new Color(getColorValue(0.6f),getColorValue(0.6f),0f,getColorValue(0.9f));
		shader.begin();
		shader.setUniformf("ourColor", currentColor);
		shader.end();

		batch.setShader(shader);
		batch.begin();
		batch.draw(texture0, 10, 10);
		batch.end();

//		batch.setShader(null);
//		batch.begin();
//		batch.draw(background, 250, 200);
//		batch.end();

		defaultBatch.begin();
		defaultBatch.draw(background, 250, 200);
		defaultBatch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
		batch.dispose();
		shader.dispose();
		texture0.dispose();
		texture1.dispose();
		mask.dispose();
	}

	private float getColorValue(float offset) {
		return (float) ((0.1f*(Math.sin(20*stateTime + 20*offset + Math.random()) / 2f) + 0.05f) + 0.9f);
	}
}
