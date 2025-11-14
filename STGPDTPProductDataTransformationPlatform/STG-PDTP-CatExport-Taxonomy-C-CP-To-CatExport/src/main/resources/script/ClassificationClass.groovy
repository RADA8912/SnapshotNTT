package src.main.resources.script

class ClassificationClass {
	final json
	final children = new ArrayList<ClassificationClass>()

	ClassificationClass(json) {
		this.json = json
	}
}
